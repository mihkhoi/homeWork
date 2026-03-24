package com.example.shop.service;

import com.example.shop.entity.Product;
import com.example.shop.entity.Wishlist;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service public class WishlistService {

  private
    final WishlistRepository wishlistRepo;
  private
    final ProductRepository productRepo;
  private
    final UserRepository userRepo;

  public
    WishlistService(WishlistRepository wishlistRepo,
                    ProductRepository productRepo,
                    UserRepository userRepo) {
        this.wishlistRepo = wishlistRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
    }

  public
    List<Product> getWishlistProducts(Long userId) {
        if (userId == null) {
            throw new ContractViolationException("PRE: userId null");
        }

        return wishlistRepo.findByUserIdOrderByCreatedAtDesc(userId).stream().map(w->productRepo.findById(w.getProductId()).orElse(null)).filter(Objects::nonNull).filter(Product::isActive).toList();
    }

  public
    Set<Long> getWishlistProductIds(Long userId) {
        if (userId == null) {
            return Set.of();
        }

        Set<Long> ids = new LinkedHashSet<>();
        for (Wishlist w : wishlistRepo.findByUserIdOrderByCreatedAtDesc(userId)) {
            ids.add(w.getProductId());
        }
        return ids;
    }

  public
    boolean isWished(Long userId, Long productId) {
        if (userId == null || productId == null) {
            return false;
        }
        return wishlistRepo.existsByUserIdAndProductId(userId, productId);
    }

    @Transactional public Wishlist addToWishlist(Long userId, Long productId) {
        if (userId == null) {
            throw new ContractViolationException("PRE: userId null");
        }
        if (productId == null) {
            throw new ContractViolationException("PRE: productId null");
        }
        if (!userRepo.existsById(userId)) {
            throw new ContractViolationException("PRE: user not found");
        }

        Product product = productRepo.findById(productId)
                              .orElseThrow(()->new ContractViolationException("PRE: product not found"));

        if (!product.isActive()) {
            throw new ContractViolationException("INV: product inactive");
        }

        if (wishlistRepo.existsByUserIdAndProductId(userId, productId)) {
            throw new ContractViolationException("INV: product already exists in wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setProductId(productId);
        wishlist.setCreatedAt(LocalDateTime.now());

        Wishlist saved = wishlistRepo.save(wishlist);

        if (saved.getId() == null) {
            throw new ContractViolationException("POST: wishlist id must exist");
        }

        return saved;
    }

    @Transactional public void removeFromWishlist(Long userId, Long productId) {
        if (userId == null) {
            throw new ContractViolationException("PRE: userId null");
        }
        if (productId == null) {
            throw new ContractViolationException("PRE: productId null");
        }

        if (!wishlistRepo.existsByUserIdAndProductId(userId, productId)) {
            throw new ContractViolationException("PRE: wishlist item not found");
        }

        wishlistRepo.deleteByUserIdAndProductId(userId, productId);

        if (wishlistRepo.existsByUserIdAndProductId(userId, productId)) {
            throw new ContractViolationException("POST: wishlist item must be removed");
        }
    }
}
