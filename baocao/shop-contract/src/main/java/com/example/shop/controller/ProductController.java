package com.example.shop.controller;

import com.example.shop.entity.Product;
import com.example.shop.repository.ProductRepository;
import com.example.shop.service.CartService;
import com.example.shop.service.ReviewService;
import com.example.shop.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Controller public class ProductController {

  private
    final ProductRepository productRepo;
  private
    final CartService cartService;
  private
    final ReviewService reviewService;
  private
    final WishlistService wishlistService;

  public
    ProductController(ProductRepository productRepo,
                      CartService cartService,
                      ReviewService reviewService,
                      WishlistService wishlistService) {
        this.productRepo = productRepo;
        this.cartService = cartService;
        this.reviewService = reviewService;
        this.wishlistService = wishlistService;
    }

  private
    Long currentUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        return userId == null ? 1L : (Long)userId;
    }

  private
    Long loggedInUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        return userId == null ? null : (Long)userId;
    }

    @GetMapping("/products") public String products(@RequestParam(required = false) String q,
                                                    @RequestParam(required = false, defaultValue = "newest") String sort,
                                                    @RequestParam(required = false) String category,
                                                    @RequestParam(required = false) String minPrice,
                                                    @RequestParam(required = false) String maxPrice,
                                                    @RequestParam(required = false, defaultValue = "false") boolean inStock,
                                                    Model model,
                                                    HttpSession session) {

        List<Product> activeProducts = new ArrayList<>(productRepo.findByActiveTrue());

        var categories = activeProducts.stream()
                             .map(p->normalizeCategory(p.getCategory()))
                             .distinct()
                             .sorted(String.CASE_INSENSITIVE_ORDER)
                             .toList();

        String keyword = q == null ? "" : q.trim();
        String selectedCategory = category == null ? "" : category.trim();
        Long min = parseLong(minPrice);
        Long max = parseLong(maxPrice);

        List<Product> filtered = activeProducts.stream()
                                     .filter(p->keyword.isBlank() || containsIgnoreCase(p.getName(), keyword) || containsIgnoreCase(p.getDescription(), keyword) || containsIgnoreCase(p.getCategory(), keyword))
                                     .filter(p->selectedCategory.isBlank() || normalizeCategory(p.getCategory()).equalsIgnoreCase(selectedCategory))
                                     .filter(p->min == null || p.getPrice() >= min)
                                     .filter(p->max == null || p.getPrice() <= max)
                                     .filter(p->!inStock || p.getStock() > 0)
                                     .toList();

        filtered = switch (sort) {
            case "priceAsc" -> filtered.stream()
                    .sorted(Comparator.comparingLong(Product::getPrice))
                    .toList();
            case "priceDesc" -> filtered.stream()
                    .sorted(Comparator.comparingLong(Product::getPrice).reversed())
                    .toList();
            case "name" -> filtered.stream()
                    .sorted(Comparator.comparing(Product::getName, String.CASE_INSENSITIVE_ORDER))
                    .toList();
            case "stockDesc" -> filtered.stream()
                    .sorted(Comparator.comparingInt(Product::getStock).reversed())
                    .toList();
            default -> filtered.stream()
                    .sorted(Comparator.comparing(Product::getId).reversed())
                    .toList();
        };

        Long loggedInUserId = loggedInUserId(session);
        Set<Long> wishlistIds = loggedInUserId == null
                ? Set.of()
                :
                wishlistService.getWishlistProductIds(loggedInUserId);

                model.addAttribute("products", filtered);
                model.addAttribute("categories", categories);
                model.addAttribute("q", keyword);
                model.addAttribute("sort", sort);
                model.addAttribute("category", selectedCategory);
                model.addAttribute("minPrice", minPrice == null ? "" : minPrice.trim());
                model.addAttribute("maxPrice", maxPrice == null ? "" : maxPrice.trim());
                model.addAttribute("inStock", inStock);
                model.addAttribute("resultCount", filtered.size());
                model.addAttribute("wishlistIds", wishlistIds);
                model.addAttribute("cartCount", cartService.getCart(currentUserId(session)).size());
                model.addAttribute("userEmail", session.getAttribute("userEmail"));
                model.addAttribute("role", session.getAttribute("role"));
                return "products";
            }

            @GetMapping("/products/{id}") public String detail(@PathVariable Long id,
                                                               Model model,
                                                               HttpSession session) {
                var p = productRepo.findById(id).orElse(null);

                if (p == null || !p.isActive()) {
                    model.addAttribute("message", "Không tìm thấy sản phẩm");
                    model.addAttribute("cartCount", cartService.getCart(currentUserId(session)).size());
                    return "product_not_found";
                }

                var relatedProducts = productRepo.findByActiveTrue().stream().filter(other->!other.getId().equals(p.getId())).filter(other->normalizeCategory(other.getCategory()).equalsIgnoreCase(normalizeCategory(p.getCategory()))).sorted(Comparator.comparing(Product::getId).reversed()).limit(4).toList();

                Long loggedInUserId = loggedInUserId(session);

                model.addAttribute("p", p);
                model.addAttribute("relatedProducts", relatedProducts);
                model.addAttribute("reviews", reviewService.getReviewsForProduct(id));
                model.addAttribute("reviewCount", reviewService.getReviewCount(id));
                model.addAttribute("averageRating", reviewService.getAverageRating(id));
                model.addAttribute("canReview", reviewService.canReview(loggedInUserId, id));
                model.addAttribute("isWished", loggedInUserId != null && wishlistService.isWished(loggedInUserId, id));
                model.addAttribute("cartCount", cartService.getCart(currentUserId(session)).size());
                model.addAttribute("userEmail", session.getAttribute("userEmail"));
                model.addAttribute("role", session.getAttribute("role"));
                return "product_detail";
            }

          private
            boolean containsIgnoreCase(String source, String keyword) {
                return source != null && source.toLowerCase().contains(keyword.toLowerCase());
            }

          private
            Long parseLong(String value) {
                if (value == null || value.isBlank()) {
                    return null;
                }
                try {
                    long parsed = Long.parseLong(value.trim());
                    return parsed < 0 ? null : parsed;
                } catch (NumberFormatException e) {
                    return null;
                }
            }

          private
            String normalizeCategory(String category) {
                if (category == null || category.isBlank()) {
                    return "General";
                }
                return category.trim();
            }
    }
