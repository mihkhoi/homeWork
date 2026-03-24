package com.example.shop.entity;

import jakarta.persistence.*;

@Entity
    @Table(name = "order_items") public class OrderItem {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @Column(name = "order_id", nullable = false) private Long orderId;

    @Column(name = "product_id", nullable = false) private Long productId;

    @Column(nullable = false) private long price;

    @Column(nullable = false) private int quantity;

  public
    Long getId() {
        return id;
    }
  public
    Long getOrderId() {
        return orderId;
    }
  public
    Long getProductId() {
        return productId;
    }
  public
    long getPrice() {
        return price;
    }
  public
    int getQuantity() {
        return quantity;
    }

  public
    void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
  public
    void setProductId(Long productId) {
        this.productId = productId;
    }
  public
    void setPrice(long price) {
        this.price = price;
    }
  public
    void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
