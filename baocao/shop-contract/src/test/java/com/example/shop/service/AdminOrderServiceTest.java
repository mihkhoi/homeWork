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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminOrderServiceTest {

  private
    OrderRepository orderRepo;
  private
    UserRepository userRepo;
  private
    OrderItemRepository orderItemRepo;
  private
    ProductRepository productRepo;
  private
    AdminOrderService adminOrderService;

    @BeforeEach void setup() {
        orderRepo = mock(OrderRepository.class);
        userRepo = mock(UserRepository.class);
        orderItemRepo = mock(OrderItemRepository.class);
        productRepo = mock(ProductRepository.class);
        adminOrderService = new AdminOrderService(orderRepo, userRepo, orderItemRepo, productRepo);
    }

    @Test void updateStatus_orderIdNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->adminOrderService.updateStatus(null, "PENDING"));
    }

    @Test void updateStatus_statusBlank_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->adminOrderService.updateStatus(1L, "   "));
    }

    @Test void updateStatus_statusInvalid_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->adminOrderService.updateStatus(1L, "HELLO"));
    }

    @Test void updateStatus_orderNotFound_shouldThrow_PRE() {
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContractViolationException.class,
                     ()->adminOrderService.updateStatus(1L, "CONFIRMED"));
    }

    @Test void updateStatus_invalidTransition_shouldThrow_INV() {
        Order order = new Order();
        order.setUserId(1L);
        order.setAddress("123 abc");
        order.setTotal(500000);
        order.setStatus("PENDING");

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ContractViolationException.class,
                     ()->adminOrderService.updateStatus(1L, "COMPLETED"));
    }

    @Test void updateStatus_valid_shouldUpdate() {
        Order order = new Order();
        order.setUserId(1L);
        order.setAddress("123 abc");
        order.setTotal(500000);
        order.setStatus("SHIPPING");

        try {
            var f = Order.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(order, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv->inv.getArgument(0));

        adminOrderService.updateStatus(1L, "completed");

        assertEquals("COMPLETED", order.getStatus());
        verify(orderRepo).save(order);
    }

    @Test void updateStatus_pendingToCancelled_shouldRestoreStock() {
        Order order = new Order();
        order.setUserId(1L);
        order.setAddress("123 abc");
        order.setTotal(500000);
        order.setStatus("PENDING");

        OrderItem item = new OrderItem();
        item.setOrderId(1L);
        item.setProductId(10L);
        item.setQuantity(3);
        item.setPrice(100000);

        Product product = new Product();
        product.setName("X");
        product.setPrice(100000);
        product.setStock(5);
        product.setActive(true);

        try {
            var pf = Product.class.getDeclaredField("id");
            pf.setAccessible(true);
            pf.set(product, 10L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepo.findByOrderId(1L)).thenReturn(List.of(item));
        when(productRepo.findById(10L)).thenReturn(Optional.of(product));
        when(orderRepo.save(any(Order.class))).thenAnswer(inv->inv.getArgument(0));

        adminOrderService.updateStatus(1L, "CANCELLED");

        assertEquals("CANCELLED", order.getStatus());
        assertEquals(8, product.getStock());
        verify(productRepo).save(product);
        verify(orderRepo).save(order);
    }

    @Test void toggleUserStatus_userIdNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->adminOrderService.toggleUserStatus(null));
    }

    @Test void toggleUserStatus_userNotFound_shouldThrow_PRE() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContractViolationException.class,
                     ()->adminOrderService.toggleUserStatus(1L));
    }

    @Test void toggleUserStatus_adminRole_shouldThrow_INV() {
        User admin = new User();
        admin.setRole("ADMIN");
        admin.setStatus("ACTIVE");

        when(userRepo.findById(1L)).thenReturn(Optional.of(admin));

        assertThrows(ContractViolationException.class,
                     ()->adminOrderService.toggleUserStatus(1L));
    }

    @Test void toggleUserStatus_activeToInactive_shouldUpdate() {
        User user = new User();
        user.setRole("USER");
        user.setStatus("ACTIVE");

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        adminOrderService.toggleUserStatus(1L);

        assertEquals("INACTIVE", user.getStatus());
        verify(userRepo).save(user);
    }

    @Test void toggleUserStatus_inactiveToActive_shouldUpdate() {
        User user = new User();
        user.setRole("USER");
        user.setStatus("INACTIVE");

        when(userRepo.findById(2L)).thenReturn(Optional.of(user));

        adminOrderService.toggleUserStatus(2L);

        assertEquals("ACTIVE", user.getStatus());
        verify(userRepo).save(user);
    }
}
