package com.example.shop.entity;

import jakarta.persistence.*;

@Entity
    @Table(name = "products") public class Product {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @Column(nullable = false) private String name;

    @Column(nullable = false, length = 1500) private String description = "";

    @Column(nullable = false) private String category = "General";

    @Column(nullable = false) private String imageUrl = "";

    @Column(nullable = false) private long price;

    @Column(nullable = false) private int stock;

    @Column(nullable = false) private boolean active = true;

  public
    Long getId() {
        return id;
    }
  public
    String getName() {
        return name;
    }
  public
    String getDescription() {
        return description;
    }
  public
    String getCategory() {
        return category;
    }
  public
    String getImageUrl() {
        return imageUrl;
    }
  public
    long getPrice() {
        return price;
    }
  public
    int getStock() {
        return stock;
    }
  public
    boolean isActive() {
        return active;
    }

  public
    void setName(String name) {
        this.name = name;
    }
  public
    void setDescription(String description) {
        this.description = description;
    }
  public
    void setCategory(String category) {
        this.category = category;
    }
  public
    void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
  public
    void setPrice(long price) {
        this.price = price;
    }
  public
    void setStock(int stock) {
        this.stock = stock;
    }
  public
    void setActive(boolean active) {
        this.active = active;
    }
}
