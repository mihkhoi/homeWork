package com.example.shop.service;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import com.example.shop.entity.Product;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderHistoryServiceTest {

  private
    OrderRepository orderRepo;
  private
    OrderItemRepository orderItemRepo;
  private
    ProductRepository productRepo;
  private
    OrderHistoryService orderHistoryService;

    @BeforeEach void setup() {
        orderRepo = mock(OrderRepository.class);
        orderItemRepo = mock(OrderItemRepository.class);
        productRepo = mock(ProductRepository.class);
        orderHistoryService = new OrderHistoryService(orderRepo, orderItemRepo, productRepo);
    }

    @Test void cancelOrder_userIdNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->orderHistoryService.cancelOrder(null, 1L));
    }

    @Test void cancelOrder_orderIdNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->orderHistoryService.cancelOrder(1L, null));
    }

    @Test void cancelOrder_orderNotFound_shouldThrow_PRE() {
        when(orderRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContractViolationException.class,
                     ()->orderHistoryService.cancelOrder(1L, 1L));
    }

    @Test void cancelOrder_orderBelongsToAnotherUser_shouldThrow_PRE() {
        Order order = new Order();
        order.setUserId(2L);
        order.setStatus("PENDING");
        order.setTotal(100000);

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ContractViolationException.class,
                     ()->orderHistoryService.cancelOrder(1L, 1L));
    }

    @Test void cancelOrder_shipping_shouldThrow_INV() {
        Order order = new Order();
        order.setUserId(1L);
        order.setStatus("SHIPPING");
        order.setTotal(100000);

        when(orderRepo.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ContractViolationException.class,
                     ()->orderHistoryService.cancelOrder(1L, 1L));
    }

    @Test void cancelOrder_valid_shouldSetCancelledAndRestoreStock() {
        Order order = new Order();
        order.setUserId(1L);
        order.setStatus("PENDING");
        order.setTotal(100000);

        OrderItem item = new OrderItem();
        item.setOrderId(1L);
        item.setProductId(10L);
        item.setQuantity(2);
        item.setPrice(50000);

        Product product = new Product();
        product.setName("X");
        product.setPrice(50000);
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

        orderHistoryService.cancelOrder(1L, 1L);

        assertEquals("CANCELLED", order.getStatus());
        assertEquals(7, product.getStock());
        verify(productRepo).save(product);
        verify(orderRepo).save(order);
    }
}
