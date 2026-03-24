package com.example.shop.controller;

import com.example.shop.exception.ContractViolationException;
import com.example.shop.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller public class ReviewController {

  private
    final ReviewService reviewService;

  public
    ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

  private
    Long loggedInUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        return userId == null ? null : (Long)userId;
    }

    @PostMapping("/products/{id}/reviews") public String createReview(@PathVariable Long id,
                                                                      @RequestParam int rating,
                                                                      @RequestParam String comment,
                                                                      HttpSession session,
                                                                      RedirectAttributes redirectAttributes) {

        Long userId = loggedInUserId(session);
        if (userId == null) {
            redirectAttributes.addFlashAttribute("errorReview", "Vui lòng đăng nhập để đánh giá sản phẩm.");
            return "redirect:/login";
        }

        try {
            reviewService.createReview(userId, id, rating, comment);
            redirectAttributes.addFlashAttribute("successReview", "Đánh giá của bạn đã được ghi nhận.");
        } catch (ContractViolationException ex) {
            redirectAttributes.addFlashAttribute("errorReview", ex.getMessage());
        }

        return "redirect:/products/" + id;
    }
}
