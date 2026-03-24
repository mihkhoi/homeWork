package com.example.shop.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
    @Table(
        name = "wishlists",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"})) public class Wishlist {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @Column(name = "user_id", nullable = false) private Long userId;

    @Column(name = "product_id", nullable = false) private Long productId;

    @Column(name = "created_at", nullable = false) private LocalDateTime createdAt;

  public
    Long getId() {
        return id;
    }

  public
    Long getUserId() {
        return userId;
    }

  public
    Long getProductId() {
        return productId;
    }

  public
    LocalDateTime getCreatedAt() {
        return createdAt;
    }

  public
    void setUserId(Long userId) {
        this.userId = userId;
    }

  public
    void setProductId(Long productId) {
        this.productId = productId;
    }

  public
    void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
