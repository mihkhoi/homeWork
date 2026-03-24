package com.example.shop.controller;

import com.example.shop.exception.ContractViolationException;
import com.example.shop.service.CartService;
import com.example.shop.service.WishlistService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller public class WishlistController {

  private
    final WishlistService wishlistService;
  private
    final CartService cartService;

  public
    WishlistController(WishlistService wishlistService, CartService cartService) {
        this.wishlistService = wishlistService;
        this.cartService = cartService;
    }

  private
    Long loggedInUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        return userId == null ? null : (Long)userId;
    }

  private
    String safeRedirect(String redirectTo) {
        if (redirectTo == null || redirectTo.isBlank() || !redirectTo.startsWith("/")) {
            return "/wishlist";
        }
        return redirectTo;
    }

    @GetMapping("/wishlist") public String wishlist(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = loggedInUserId(session);
        if (userId == null) {
            redirectAttributes.addFlashAttribute("errorWishlist", "Vui lòng đăng nhập để xem danh sách yêu thích.");
            return "redirect:/login";
        }

        var products = wishlistService.getWishlistProducts(userId);

        model.addAttribute("products", products);
        model.addAttribute("wishlistIds", wishlistService.getWishlistProductIds(userId));
        model.addAttribute("wishlistCount", products.size());
        model.addAttribute("cartCount", cartService.getCart(userId).size());
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("role", session.getAttribute("role"));
        return "wishlist";
    }

    @PostMapping("/wishlist/add") public String add(@RequestParam Long productId,
                                                    @RequestParam(required = false) String redirectTo,
                                                    HttpSession session,
                                                    RedirectAttributes redirectAttributes) {
        Long userId = loggedInUserId(session);
        if (userId == null) {
            redirectAttributes.addFlashAttribute("errorWishlist", "Vui lòng đăng nhập để thêm sản phẩm yêu thích.");
            return "redirect:/login";
        }

        try {
            wishlistService.addToWishlist(userId, productId);
            redirectAttributes.addFlashAttribute("successWishlist", "Đã thêm vào danh sách yêu thích.");
        } catch (ContractViolationException ex) {
            redirectAttributes.addFlashAttribute("errorWishlist", ex.getMessage());
        }

        return "redirect:" + safeRedirect(redirectTo);
    }

    @PostMapping("/wishlist/remove") public String remove(@RequestParam Long productId,
                                                          @RequestParam(required = false) String redirectTo,
                                                          HttpSession session,
                                                          RedirectAttributes redirectAttributes) {
        Long userId = loggedInUserId(session);
        if (userId == null) {
            redirectAttributes.addFlashAttribute("errorWishlist", "Vui lòng đăng nhập để thao tác wishlist.");
            return "redirect:/login";
        }

        try {
            wishlistService.removeFromWishlist(userId, productId);
            redirectAttributes.addFlashAttribute("successWishlist", "Đã xóa khỏi danh sách yêu thích.");
        } catch (ContractViolationException ex) {
            redirectAttributes.addFlashAttribute("errorWishlist", ex.getMessage());
        }

        return "redirect:" + safeRedirect(redirectTo);
    }
}
