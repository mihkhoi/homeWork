package com.example.shop.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
    @Table(name = "reviews") public class Review {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @Column(name = "user_id", nullable = false) private Long userId;

    @Column(name = "product_id", nullable = false) private Long productId;

    @Column(name = "reviewer_name", nullable = false, length = 120) private String reviewerName;

    @Column(nullable = false) private int rating;

    @Column(nullable = false, length = 500) private String comment;

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
    String getReviewerName() {
        return reviewerName;
    }

  public
    int getRating() {
        return rating;
    }

  public
    String getComment() {
        return comment;
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
    void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

  public
    void setRating(int rating) {
        this.rating = rating;
    }

  public
    void setComment(String comment) {
        this.comment = comment;
    }

  public
    void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
