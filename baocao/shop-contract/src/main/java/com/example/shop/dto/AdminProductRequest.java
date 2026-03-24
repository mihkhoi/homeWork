package com.example.shop.dto;

public
class AdminProductRequest {
  private
    String name;
  private
    String description;
  private
    String category;
  private
    String imageUrl;
  private
    long price;
  private
    int stock;
  private
    boolean active = true;

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
