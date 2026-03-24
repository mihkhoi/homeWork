package com.example.shop.service;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import com.example.shop.entity.Product;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service public class OrderHistoryService {

  private
    final OrderRepository orderRepo;
  private
    final OrderItemRepository orderItemRepo;
  private
    final ProductRepository productRepo;

  public
    OrderHistoryService(OrderRepository orderRepo,
                        OrderItemRepository orderItemRepo,
                        ProductRepository productRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.productRepo = productRepo;
    }

  public
    boolean canCancel(Order order) {
        if (order == null || order.getStatus() == null) {
            return false;
        }
        String status = normalizeStatus(order.getStatus());
        return "PENDING".equals(status) || "CONFIRMED".equals(status);
    }

    @Transactional public void cancelOrder(Long userId, Long orderId) {
        if (userId == null) {
            throw new ContractViolationException("PRE: userId null");
        }
        if (orderId == null) {
            throw new ContractViolationException("PRE: orderId null");
        }

        Order order = orderRepo.findById(orderId)
                          .orElseThrow(()->new ContractViolationException("PRE: order not found"));

        if (!order.getUserId().equals(userId)) {
            throw new ContractViolationException("PRE: order does not belong to current user");
        }

        if (order.getTotal() < 0) {
            throw new ContractViolationException("INV: order total must be >= 0");
        }

        if (!canCancel(order)) {
            throw new ContractViolationException("INV: only PENDING or CONFIRMED orders can be cancelled");
        }

        List<OrderItem> items = orderItemRepo.findByOrderId(orderId);
        if (items.isEmpty()) {
            throw new ContractViolationException("INV: order items must not be empty");
        }

        for (OrderItem item : items) {
            if (item.getQuantity() < 1) {
                throw new ContractViolationException("INV: order item quantity must be >= 1");
            }

            Product product = productRepo.findById(item.getProductId())
                                  .orElseThrow(()->new ContractViolationException("INV: product missing when cancelling order"));

            product.setStock(product.getStock() + item.getQuantity());

            if (product.getStock() < 0) {
                throw new ContractViolationException("INV: product stock must be >= 0");
            }

            productRepo.save(product);
        }

        order.setStatus("CANCELLED");
        Order saved = orderRepo.save(order);

        if (!"CANCELLED".equals(saved.getStatus())) {
            throw new ContractViolationException("POST: order status must be CANCELLED");
        }
    }

  private
    String normalizeStatus(String status) {
        return status == null ? "" : status.trim().toUpperCase();
    }
}
