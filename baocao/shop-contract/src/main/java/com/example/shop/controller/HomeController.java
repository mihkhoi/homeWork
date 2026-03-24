package com.example.shop.controller;

import com.example.shop.repository.ProductRepository;
import com.example.shop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;

@Controller public class HomeController {

  private
    final ProductRepository productRepo;
  private
    final CartService cartService;

  public
    HomeController(ProductRepository productRepo, CartService cartService) {
        this.productRepo = productRepo;
        this.cartService = cartService;
    }

  private
    Long currentUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        return userId == null ? 1L : (Long)userId;
    }

    @GetMapping("/") public String home(Model model, HttpSession session) {
        var activeProducts = productRepo.findByActiveTrue();

        var featuredProducts = activeProducts.stream()
                                   .sorted(Comparator.comparing(com.example.shop.entity.Product::getId).reversed())
                                   .limit(4)
                                   .toList();

        var categories = activeProducts.stream()
                             .map(p->normalizeCategory(p.getCategory()))
                             .distinct()
                             .sorted(String.CASE_INSENSITIVE_ORDER)
                             .toList();

        var cheapProducts = activeProducts.stream()
                                .sorted(Comparator.comparingLong(com.example.shop.entity.Product::getPrice))
                                .limit(4)
                                .toList();

        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("role", session.getAttribute("role"));
        model.addAttribute("fullName", session.getAttribute("fullName"));
        model.addAttribute("cartCount", cartService.getCart(currentUserId(session)).size());

        model.addAttribute("featuredProducts", featuredProducts);
        model.addAttribute("cheapProducts", cheapProducts);
        model.addAttribute("categories", categories);
        model.addAttribute("productCount", activeProducts.size());

        return "home";
    }

  private
    String normalizeCategory(String category) {
        if (category == null || category.isBlank()) {
            return "General";
        }
        return category.trim();
    }
}
