package com.example.shop.service;

import com.example.shop.entity.Order;
import com.example.shop.entity.OrderItem;
import com.example.shop.entity.Product;
import com.example.shop.entity.Review;
import com.example.shop.entity.User;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.ReviewRepository;
import com.example.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

  private
    ReviewRepository reviewRepo;
  private
    ProductRepository productRepo;
  private
    UserRepository userRepo;
  private
    OrderRepository orderRepo;
  private
    OrderItemRepository orderItemRepo;
  private
    ReviewService reviewService;

    @BeforeEach void setup() {
        reviewRepo = mock(ReviewRepository.class);
        productRepo = mock(ProductRepository.class);
        userRepo = mock(UserRepository.class);
        orderRepo = mock(OrderRepository.class);
        orderItemRepo = mock(OrderItemRepository.class);

        reviewService = new ReviewService(
            reviewRepo, productRepo, userRepo, orderRepo, orderItemRepo);
    }

    @Test void createReview_ratingInvalid_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->reviewService.createReview(1L, 10L, 6, "good"));
    }

    @Test void createReview_blankComment_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->reviewService.createReview(1L, 10L, 5, "   "));
    }

    @Test void createReview_alreadyReviewed_shouldThrow_INV() {
        Product product = new Product();
        User user = new User();
        user.setFullName("Nem Lui");
        user.setEmail("nem@example.com");

        when(productRepo.findById(10L)).thenReturn(Optional.of(product));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepo.existsByUserIdAndProductId(1L, 10L)).thenReturn(true);

        assertThrows(ContractViolationException.class,
                     ()->reviewService.createReview(1L, 10L, 5, "Rất tốt"));
    }

    @Test void createReview_notPurchased_shouldThrow_PRE() {
        Product product = new Product();
        User user = new User();
        user.setFullName("Nem Lui");
        user.setEmail("nem@example.com");

        when(productRepo.findById(10L)).thenReturn(Optional.of(product));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepo.existsByUserIdAndProductId(1L, 10L)).thenReturn(false);
        when(orderRepo.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of());

        assertThrows(ContractViolationException.class,
                     ()->reviewService.createReview(1L, 10L, 5, "Rất tốt"));
    }

    @Test void createReview_valid_shouldSaveReview() {
        Product product = new Product();
        User user = new User();
        user.setFullName("Nem Lui");
        user.setEmail("nem@example.com");

        Order order = new Order();
        order.setUserId(1L);
        order.setStatus("COMPLETED");

        try {
            var of = Order.class.getDeclaredField("id");
            of.setAccessible(true);
            of.set(order, 100L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        OrderItem item = new OrderItem();
        item.setOrderId(100L);
        item.setProductId(10L);
        item.setQuantity(1);
        item.setPrice(99000);

        when(productRepo.findById(10L)).thenReturn(Optional.of(product));
        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepo.existsByUserIdAndProductId(1L, 10L)).thenReturn(false);
        when(orderRepo.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(List.of(order));
        when(orderItemRepo.findByOrderId(100L)).thenReturn(List.of(item));

        when(reviewRepo.save(any(Review.class))).thenAnswer(inv->{
            Review r = inv.getArgument(0);
            try {
                var rf = Review.class.getDeclaredField("id");
                rf.setAccessible(true);
                rf.set(r, 1L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return r;
        });

        Review saved = reviewService.createReview(1L, 10L, 5, "Sản phẩm rất ổn");

        assertNotNull(saved.getId());
        assertEquals(5, saved.getRating());
        assertEquals("Sản phẩm rất ổn", saved.getComment());
        assertEquals("Nem Lui", saved.getReviewerName());
    }
}
