package com.example.shop.entity;

import jakarta.persistence.*;

@Entity
    @Table(
        name = "cart_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "product_id"})) public class CartItem {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @Column(name = "user_id", nullable = false) private Long userId;

    @Column(name = "product_id", nullable = false) private Long productId;

    @Column(nullable = false) private int quantity;

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
    int getQuantity() {
        return quantity;
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
    void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
