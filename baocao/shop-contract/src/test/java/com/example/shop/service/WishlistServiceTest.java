package com.example.shop.service;

import com.example.shop.entity.Product;
import com.example.shop.entity.Wishlist;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.repository.WishlistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

  private
    WishlistRepository wishlistRepo;
  private
    ProductRepository productRepo;
  private
    UserRepository userRepo;
  private
    WishlistService wishlistService;

    @BeforeEach void setup() {
        wishlistRepo = mock(WishlistRepository.class);
        productRepo = mock(ProductRepository.class);
        userRepo = mock(UserRepository.class);
        wishlistService = new WishlistService(wishlistRepo, productRepo, userRepo);
    }

    @Test void addToWishlist_userIdNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->wishlistService.addToWishlist(null, 10L));
    }

    @Test void addToWishlist_productIdNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->wishlistService.addToWishlist(1L, null));
    }

    @Test void addToWishlist_duplicate_shouldThrow_INV() {
        when(userRepo.existsById(1L)).thenReturn(true);

        Product p = new Product();
        p.setActive(true);
        when(productRepo.findById(10L)).thenReturn(Optional.of(p));
        when(wishlistRepo.existsByUserIdAndProductId(1L, 10L)).thenReturn(true);

        assertThrows(ContractViolationException.class,
                     ()->wishlistService.addToWishlist(1L, 10L));
    }

    @Test void addToWishlist_valid_shouldSave() {
        when(userRepo.existsById(1L)).thenReturn(true);

        Product p = new Product();
        p.setActive(true);
        when(productRepo.findById(10L)).thenReturn(Optional.of(p));
        when(wishlistRepo.existsByUserIdAndProductId(1L, 10L)).thenReturn(false);

        when(wishlistRepo.save(any(Wishlist.class))).thenAnswer(inv->{
            Wishlist w = inv.getArgument(0);
            try {
                var f = Wishlist.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(w, 1L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return w;
        });

        Wishlist saved = wishlistService.addToWishlist(1L, 10L);

        assertNotNull(saved.getId());
        assertEquals(1L, saved.getUserId());
        assertEquals(10L, saved.getProductId());
    }

    @Test void removeFromWishlist_notFound_shouldThrow_PRE() {
        when(wishlistRepo.existsByUserIdAndProductId(1L, 10L)).thenReturn(false);

        assertThrows(ContractViolationException.class,
                     ()->wishlistService.removeFromWishlist(1L, 10L));
    }

    @Test void removeFromWishlist_valid_shouldDelete() {
        when(wishlistRepo.existsByUserIdAndProductId(1L, 10L))
            .thenReturn(true)
            .thenReturn(false);

        wishlistService.removeFromWishlist(1L, 10L);

        verify(wishlistRepo).deleteByUserIdAndProductId(1L, 10L);
    }
}
