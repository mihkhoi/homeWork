package com.example.shop.service;

import com.example.shop.entity.CartItem;
import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import com.example.shop.entity.Product;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service public class CheckoutService {

  private
    final CartItemRepository cartRepo;
  private
    final ProductRepository productRepo;
  private
    final OrderRepository orderRepo;
  private
    final OrderItemRepository orderItemRepo;
  private
    final VoucherService voucherService;

  public
    CheckoutService(CartItemRepository cartRepo,
                    ProductRepository productRepo,
                    OrderRepository orderRepo,
                    OrderItemRepository orderItemRepo,
                    VoucherService voucherService) {
        this.cartRepo = cartRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.voucherService = voucherService;
    }

    @Transactional public Order checkout(Long userId, String address) {
        return checkout(userId, address, null);
    }

    @Transactional public Order checkout(Long userId, String address, String voucherCode) {
        if (userId == null) {
            throw new ContractViolationException("PRE: userId null");
        }
        if (address == null || address.isBlank()) {
            throw new ContractViolationException("PRE: address blank");
        }

        List<CartItem> cart = cartRepo.findByUserId(userId);
        if (cart.isEmpty()) {
            throw new ContractViolationException("PRE: cart is empty");
        }

        long subtotal = 0;
        long shippingFee = 30000;

        for (CartItem ci : cart) {
            if (ci.getQuantity() < 1) {
                throw new ContractViolationException("INV: quantity must be >= 1");
            }

            Product p = productRepo.findById(ci.getProductId())
                            .orElseThrow(()->new ContractViolationException("PRE: product not found"));

            if (!p.isActive()) {
                throw new ContractViolationException("INV: product inactive");
            }
            if (p.getPrice() < 0) {
                throw new ContractViolationException("INV: price must be >= 0");
            }
            if (p.getStock() < ci.getQuantity()) {
                throw new ContractViolationException("PRE: insufficient stock");
            }

            subtotal += p.getPrice() * ci.getQuantity();
        }

        long discount = voucherService.calculateDiscount(voucherCode, subtotal);
        long total = subtotal + shippingFee - discount;

        if (discount < 0) {
            throw new ContractViolationException("INV: discount must be >= 0");
        }
        if (total < 0) {
            throw new ContractViolationException("INV: total must be >= 0");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setAddress(address.trim());
        order.setShippingFee(shippingFee);
        order.setPaymentMethod("COD");
        order.setStatus("PENDING");
        order.setVoucherCode(voucherCode == null || voucherCode.isBlank() ? null : voucherCode.trim().toUpperCase());
        order.setDiscountAmount(discount);
        order.setTotal(total);
        order.setCreatedAt(LocalDateTime.now());

        Order saved = orderRepo.save(order);

        if (saved.getId() == null) {
            throw new ContractViolationException("POST: order id must exist");
        }

        for (CartItem ci : cart) {
            Product p = productRepo.findById(ci.getProductId())
                            .orElseThrow(()->new ContractViolationException("PRE: product not found"));

            p.setStock(p.getStock() - ci.getQuantity());
            if (p.getStock() < 0) {
                throw new ContractViolationException("INV: stock must be >= 0");
            }
            productRepo.save(p);

            OrderItem oi = new OrderItem();
            oi.setOrderId(saved.getId());
            oi.setProductId(p.getId());
            oi.setPrice(p.getPrice());
            oi.setQuantity(ci.getQuantity());
            orderItemRepo.save(oi);
        }

        if (voucherCode != null && !voucherCode.isBlank()) {
            voucherService.applyVoucher(voucherCode, subtotal);
        }

        cartRepo.deleteAll(cart);

        if (saved.getTotal() != total) {
            throw new ContractViolationException("POST: total mismatch");
        }

        return saved;
    }
}
