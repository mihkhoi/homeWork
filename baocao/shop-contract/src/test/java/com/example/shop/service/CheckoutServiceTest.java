package com.example.shop.service;

import com.example.shop.entity.CartItem;
import com.example.shop.entity.Order;
import com.example.shop.entity.Product;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CheckoutServiceTest {

  private
    CartItemRepository cartRepo;
  private
    ProductRepository productRepo;
  private
    OrderRepository orderRepo;
  private
    OrderItemRepository orderItemRepo;
  private
    VoucherService voucherService;
  private
    CheckoutService checkoutService;

    @BeforeEach void setup() {
        cartRepo = mock(CartItemRepository.class);
        productRepo = mock(ProductRepository.class);
        orderRepo = mock(OrderRepository.class);
        orderItemRepo = mock(OrderItemRepository.class);
        voucherService = mock(VoucherService.class);
        checkoutService = new CheckoutService(cartRepo, productRepo, orderRepo, orderItemRepo, voucherService);
    }

    @Test void checkout_savedOrderWithoutId_shouldThrow_POST() {
        CartItem ci = new CartItem();
        ci.setUserId(1L);
        ci.setProductId(10L);
        ci.setQuantity(1);

        when(cartRepo.findByUserId(1L)).thenReturn(List.of(ci));

        Product p = new Product();
        p.setName("X");
        p.setPrice(100);
        p.setStock(10);
        p.setActive(true);

        try {
            var f = Product.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(p, 10L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(productRepo.findById(10L)).thenReturn(Optional.of(p));
        when(voucherService.calculateDiscount(null, 100)).thenReturn(0L);

        Order saved = new Order();
        saved.setUserId(1L);
        saved.setAddress("123 ABC");
        saved.setTotal(30100);

        when(orderRepo.save(any(Order.class))).thenReturn(saved);

        assertThrows(ContractViolationException.class,
                     ()->checkoutService.checkout(1L, "123 ABC"));
    }

    @Test void checkout_userIdNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->checkoutService.checkout(null, "123 ABC"));
    }

    @Test void checkout_addressBlank_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->checkoutService.checkout(1L, "  "));
    }

    @Test void checkout_cartEmpty_shouldThrow_PRE() {
        when(cartRepo.findByUserId(1L)).thenReturn(List.of());

        assertThrows(ContractViolationException.class,
                     ()->checkoutService.checkout(1L, "123 ABC"));
    }

    @Test void checkout_insufficientStock_shouldThrow_PRE() {
        CartItem ci = new CartItem();
        ci.setUserId(1L);
        ci.setProductId(10L);
        ci.setQuantity(5);

        when(cartRepo.findByUserId(1L)).thenReturn(List.of(ci));

        Product p = new Product();
        p.setName("X");
        p.setPrice(100);
        p.setStock(2);
        p.setActive(true);

        try {
            var f = Product.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(p, 10L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(productRepo.findById(10L)).thenReturn(Optional.of(p));

        assertThrows(ContractViolationException.class,
                     ()->checkoutService.checkout(1L, "123 ABC"));
    }

    @Test void checkout_valid_shouldCreateOrder_reduceStock_clearCart_POST() {
        CartItem ci = new CartItem();
        ci.setUserId(1L);
        ci.setProductId(10L);
        ci.setQuantity(2);

        when(cartRepo.findByUserId(1L)).thenReturn(List.of(ci));

        Product p = new Product();
        p.setName("X");
        p.setPrice(100);
        p.setStock(10);
        p.setActive(true);

        try {
            var f = Product.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(p, 10L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(productRepo.findById(10L)).thenReturn(Optional.of(p));
        when(voucherService.calculateDiscount(null, 200)).thenReturn(0L);

        when(orderRepo.save(any(Order.class))).thenAnswer(inv->{
            Order o = inv.getArgument(0);
            Order saved = new Order();
            saved.setUserId(o.getUserId());
            saved.setAddress(o.getAddress());
            saved.setTotal(o.getTotal());
            saved.setCreatedAt(o.getCreatedAt());
            saved.setVoucherCode(o.getVoucherCode());
            saved.setDiscountAmount(o.getDiscountAmount());

            try {
                var f = Order.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(saved, 1L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return saved;
        });

        Order order = checkoutService.checkout(1L, "  123 ABC  ");

        assertNotNull(order.getId());
        assertEquals(30200, order.getTotal());
        assertEquals("123 ABC", order.getAddress());
        assertEquals(8, p.getStock());

        verify(productRepo, atLeastOnce()).save(any(Product.class));
        verify(cartRepo).deleteAll(anyList());
    }

    @Test void checkout_withVoucher_shouldCalculateCorrectTotal() {
        CartItem ci = new CartItem();
        ci.setUserId(1L);
        ci.setProductId(10L);
        ci.setQuantity(2);

        when(cartRepo.findByUserId(1L)).thenReturn(List.of(ci));

        Product p = new Product();
        p.setName("Product A");
        p.setPrice(100000);
        p.setStock(10);
        p.setActive(true);

        try {
            var f = Product.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(p, 10L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(productRepo.findById(10L)).thenReturn(Optional.of(p));
        when(voucherService.calculateDiscount("SALE10", 200000)).thenReturn(20000L);
        when(orderRepo.save(any(Order.class))).thenAnswer(inv->{
            Order o = inv.getArgument(0);
            try {
                var f = Order.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(o, 1L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return o;
        });

        Order order = checkoutService.checkout(1L, "123 ABC", "SALE10");

        assertEquals(210000, order.getTotal());
        assertEquals("SALE10", order.getVoucherCode());
        assertEquals(20000, order.getDiscountAmount());
        verify(voucherService).applyVoucher("SALE10", 200000);
    }
}
