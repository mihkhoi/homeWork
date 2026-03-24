package com.example.shop.service;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import com.example.shop.entity.Product;
import com.example.shop.entity.User;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service public class AdminOrderService {

  private
    static final Set<String> ALLOWED =
        Set.of("PENDING", "CONFIRMED", "SHIPPING", "COMPLETED", "CANCELLED");

  private
    final OrderRepository orderRepo;
  private
    final UserRepository userRepo;
  private
    final OrderItemRepository orderItemRepo;
  private
    final ProductRepository productRepo;

  public
    AdminOrderService(OrderRepository orderRepo,
                      UserRepository userRepo,
                      OrderItemRepository orderItemRepo,
                      ProductRepository productRepo) {
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.orderItemRepo = orderItemRepo;
        this.productRepo = productRepo;
    }

    @Transactional public void updateStatus(Long orderId, String status) {
        if (orderId == null) {
            throw new ContractViolationException("PRE: orderId null");
        }
        if (status == null || status.isBlank()) {
            throw new ContractViolationException("PRE: status blank");
        }

        String normalized = normalizeStatus(status);
        if (!ALLOWED.contains(normalized)) {
            throw new ContractViolationException("PRE: invalid order status");
        }

        Order order = orderRepo.findById(orderId)
                          .orElseThrow(()->new ContractViolationException("PRE: order not found"));

        if (order.getTotal() < 0) {
            throw new ContractViolationException("INV: total must be >= 0");
        }

        String current = normalizeStatus(order.getStatus());
        if (!ALLOWED.contains(current)) {
            throw new ContractViolationException("INV: current order status invalid");
        }

        if (!canTransition(current, normalized)) {
            throw new ContractViolationException("INV: invalid status transition");
        }

        if (!current.equals(normalized) && "CANCELLED".equals(normalized)) {
            restoreStock(orderId);
        }

        order.setStatus(normalized);
        Order saved = orderRepo.save(order);

        if (!normalized.equals(saved.getStatus())) {
            throw new ContractViolationException("POST: status not updated");
        }
    }

  public
    void toggleUserStatus(Long userId) {
        if (userId == null) {
            throw new ContractViolationException("PRE: userId null");
        }

        User user = userRepo.findById(userId)
                        .orElseThrow(()->new ContractViolationException("PRE: user not found"));

        if (!"USER".equals(user.getRole())) {
            throw new ContractViolationException("INV: only USER accounts can be toggled here");
        }

        user.setStatus("ACTIVE".equals(user.getStatus()) ? "INACTIVE" : "ACTIVE");
        userRepo.save(user);
    }

  private
    boolean canTransition(String current, String next) {
        if (current.equals(next)) {
            return true;
        }

        return switch (current) {
            case "PENDING" -> "CONFIRMED".equals(next) || "CANCELLED".equals(next);
            case "CONFIRMED" -> "SHIPPING".equals(next) || "CANCELLED".equals(next);
            case "SHIPPING" -> "COMPLETED".equals(next);
            case "COMPLETED", "CANCELLED" -> false;
            default -> false;
        };
    }

    private void restoreStock(Long orderId) {
        List<OrderItem> items = orderItemRepo.findByOrderId(orderId);
        if (items.isEmpty()) {
            throw new ContractViolationException("INV: order items must not be empty");
        }

        for (OrderItem item : items) {
                    if (item.getQuantity() < 1) {
                        throw new ContractViolationException("INV: order item quantity must be >= 1");
                    }

                    Product product = productRepo.findById(item.getProductId())
                                          .orElseThrow(()->new ContractViolationException("INV: product missing when restoring stock"));

                    product.setStock(product.getStock() + item.getQuantity());

                    if (product.getStock() < 0) {
                        throw new ContractViolationException("INV: stock must be >= 0");
                    }

                    productRepo.save(product);
                }
            }

          private
            String normalizeStatus(String status) {
                return status == null ? "" : status.trim().toUpperCase();
            }
    }
