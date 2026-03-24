package com.example.shop.service;

import com.example.shop.entity.CartItem;
import com.example.shop.entity.Product;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.CartItemRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

  private
    CartItemRepository cartRepo;
  private
    ProductRepository productRepo;
  private
    UserRepository userRepo;
  private
    CartService cartService;

    @BeforeEach void setup() {
        cartRepo = mock(CartItemRepository.class);
        productRepo = mock(ProductRepository.class);
        userRepo = mock(UserRepository.class);
        cartService = new CartService(cartRepo, productRepo, userRepo);

        when(userRepo.existsById(1L)).thenReturn(true);

        Product p = new Product();
        p.setName("X");
        p.setPrice(100);
        p.setStock(10);

        try {
            var f = Product.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(p, 10L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(productRepo.findById(10L)).thenReturn(Optional.of(p));
    }

    @Test void addToCart_inactiveProduct_shouldThrow_INV() {
        Product p = new Product();
        p.setName("X");
        p.setPrice(100);
        p.setStock(10);
        p.setActive(false);

        try {
            var f = Product.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(p, 10L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(productRepo.findById(10L)).thenReturn(Optional.of(p));

        assertThrows(ContractViolationException.class,
                     ()->cartService.addToCart(1L, 10L, 1));
    }

    @Test void addToCart_qtyLessThan1_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->cartService.addToCart(1L, 10L, 0));
    }

    @Test void addToCart_valid_shouldSaveNewItem() {
        when(cartRepo.findByUserIdAndProductId(1L, 10L)).thenReturn(Optional.empty());
        when(cartRepo.findByUserId(1L)).thenReturn(List.of());

        cartService.addToCart(1L, 10L, 2);

        verify(cartRepo).save(any(CartItem.class));
    }

    @Test void updateQuantity_cartEmpty_loop0_shouldThrow() {
        when(cartRepo.findByUserId(1L)).thenReturn(List.of());

        assertThrows(ContractViolationException.class,
                     ()->cartService.updateQuantity(1L, 10L, 2));
    }

    @Test void updateQuantity_oneItem_loop1_shouldUpdate() {
        CartItem ci = new CartItem();
        ci.setUserId(1L);
        ci.setProductId(10L);
        ci.setQuantity(1);

        when(cartRepo.findByUserId(1L)).thenReturn(List.of(ci));

        cartService.updateQuantity(1L, 10L, 5);

        assertEquals(5, ci.getQuantity());
        verify(cartRepo).save(ci);
    }

    @Test void updateQuantity_manyItems_loopMany_shouldUpdateLast() {
        CartItem ci1 = new CartItem();
        ci1.setUserId(1L);
        ci1.setProductId(1L);
        ci1.setQuantity(1);
        CartItem ci2 = new CartItem();
        ci2.setUserId(1L);
        ci2.setProductId(2L);
        ci2.setQuantity(1);
        CartItem ci3 = new CartItem();
        ci3.setUserId(1L);
        ci3.setProductId(10L);
        ci3.setQuantity(1);

        Product p1 = new Product();
        p1.setName("A");
        p1.setPrice(100);
        p1.setStock(10);
        Product p2 = new Product();
        p2.setName("B");
        p2.setPrice(100);
        p2.setStock(10);

        try {
            var f = Product.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(p1, 1L);
            f.set(p2, 2L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(productRepo.findById(1L)).thenReturn(Optional.of(p1));
        when(productRepo.findById(2L)).thenReturn(Optional.of(p2));
        when(productRepo.findById(10L)).thenReturn(productRepo.findById(10L));
        when(cartRepo.findByUserId(1L)).thenReturn(List.of(ci1, ci2, ci3));

        cartService.updateQuantity(1L, 10L, 7);

        assertEquals(7, ci3.getQuantity());
        verify(cartRepo).save(ci3);
    }
}
