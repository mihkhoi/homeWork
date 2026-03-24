package com.example.shop.service;

import com.example.shop.entity.Review;
import com.example.shop.entity.User;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.ReviewRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service public class ReviewService {

  private
    final ReviewRepository reviewRepo;
  private
    final ProductRepository productRepo;
  private
    final UserRepository userRepo;
  private
    final OrderRepository orderRepo;
  private
    final OrderItemRepository orderItemRepo;

  public
    ReviewService(ReviewRepository reviewRepo,
                  ProductRepository productRepo,
                  UserRepository userRepo,
                  OrderRepository orderRepo,
                  OrderItemRepository orderItemRepo) {
        this.reviewRepo = reviewRepo;
        this.productRepo = productRepo;
        this.userRepo = userRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

  public
    List<Review> getReviewsForProduct(Long productId) {
        return reviewRepo.findByProductIdOrderByCreatedAtDesc(productId);
    }

  public
    long getReviewCount(Long productId) {
        return reviewRepo.countByProductId(productId);
    }

  public
    double getAverageRating(Long productId) {
        List<Review> reviews = reviewRepo.findByProductIdOrderByCreatedAtDesc(productId);
        if (reviews.isEmpty()) {
            return 0.0;
        }

        double sum = 0;
        for (Review r : reviews) {
            sum += r.getRating();
        }
        return sum / reviews.size();
    }

  public
    boolean canReview(Long userId, Long productId) {
        if (userId == null || productId == null) {
            return false;
        }

        if (reviewRepo.existsByUserIdAndProductId(userId, productId)) {
            return false;
        }

        return hasCompletedPurchase(userId, productId);
    }

  public
    Review createReview(Long userId, Long productId, int rating, String comment) {
        if (userId == null) {
            throw new ContractViolationException("PRE: userId null");
        }
        if (productId == null) {
            throw new ContractViolationException("PRE: productId null");
        }
        if (rating < 1 || rating > 5) {
            throw new ContractViolationException("PRE: rating must be between 1 and 5");
        }

        String normalizedComment = comment == null ? "" : comment.trim();
        if (normalizedComment.isBlank()) {
            throw new ContractViolationException("PRE: comment must not be blank");
        }
        if (normalizedComment.length() > 500) {
            throw new ContractViolationException("PRE: comment length must be <= 500");
        }

        productRepo.findById(productId)
            .orElseThrow(()->new ContractViolationException("PRE: product not found"));

        User user = userRepo.findById(userId)
                        .orElseThrow(()->new ContractViolationException("PRE: user not found"));

        if (reviewRepo.existsByUserIdAndProductId(userId, productId)) {
            throw new ContractViolationException("INV: user already reviewed this product");
        }

        if (!hasCompletedPurchase(userId, productId)) {
            throw new ContractViolationException("PRE: only users with completed orders can review");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(productId);
        review.setReviewerName(
            user.getFullName() == null || user.getFullName().isBlank()
                ? user.getEmail()
                : user.getFullName().trim());
        review.setRating(rating);
        review.setComment(normalizedComment);
        review.setCreatedAt(LocalDateTime.now());

        Review saved = reviewRepo.save(review);

        if (saved.getId() == null) {
            throw new ContractViolationException("POST: review id must exist");
        }
        if (saved.getRating() != rating) {
            throw new ContractViolationException("POST: review rating mismatch");
        }

        return saved;
    }

  private
    boolean hasCompletedPurchase(Long userId, Long productId) {
        var orders = orderRepo.findByUserIdOrderByCreatedAtDesc(userId);

        for (var order : orders) {
            if (!"COMPLETED".equals(normalizeStatus(order.getStatus()))) {
                continue;
            }

            var items = orderItemRepo.findByOrderId(order.getId());
            for (var item : items) {
                if (item.getProductId().equals(productId)) {
                    return true;
                }
            }
        }

        return false;
    }

  private
    String normalizeStatus(String status) {
        return status == null ? "" : status.trim().toUpperCase();
    }
}
