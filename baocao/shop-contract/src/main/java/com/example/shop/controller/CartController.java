package com.example.shop.controller;

import com.example.shop.entity.CartItem;
import com.example.shop.repository.ProductRepository;
import com.example.shop.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller public class CartController {

  private
    final CartService cartService;
  private
    final ProductRepository productRepo;

  public
    CartController(CartService cartService, ProductRepository productRepo) {
        this.cartService = cartService;
        this.productRepo = productRepo;
    }

  private
    Long currentUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        return userId == null ? 1L : (Long)userId;
    }

    @GetMapping("/cart") public String viewCart(Model model, HttpSession session) {
        Long userId = currentUserId(session);
        List<CartItem> items = cartService.getCart(userId);

        long total = 0;
        List<Map<String, Object>> lines = new ArrayList<>();

        for (CartItem ci : items) {
            var p = productRepo.findById(ci.getProductId()).orElse(null);
            if (p == null)
                continue;

            long subtotal = p.getPrice() * ci.getQuantity();
            total += subtotal;

            Map<String, Object> line = new HashMap<>();
            line.put("productId", p.getId());
            line.put("name", p.getName());
            line.put("price", p.getPrice());
            line.put("qty", ci.getQuantity());
            line.put("subtotal", subtotal);
            lines.add(line);
        }

        model.addAttribute("lines", lines);
        model.addAttribute("total", total);
        model.addAttribute("cartCount", items.size());
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("role", session.getAttribute("role"));
        return "cart";
    }

    @PostMapping("/cart/add") public String add(@RequestParam Long productId,
                                                @RequestParam(defaultValue = "1") int qty,
                                                HttpSession session) {
        cartService.addToCart(currentUserId(session), productId, qty);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update") public String update(@RequestParam Long productId,
                                                      @RequestParam int qty,
                                                      HttpSession session) {
        cartService.updateQuantity(currentUserId(session), productId, qty);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove") public String remove(@RequestParam Long productId, HttpSession session) {
        cartService.removeItem(currentUserId(session), productId);
        return "redirect:/cart";
    }
}
