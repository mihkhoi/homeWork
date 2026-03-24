package com.example.shop.controller;

import com.example.shop.entity.CartItem;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.ProductRepository;
import com.example.shop.service.CartService;
import com.example.shop.service.CheckoutService;
import com.example.shop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller public class CheckoutController {

  private
    final CheckoutService checkoutService;
  private
    final CartService cartService;
  private
    final ProductRepository productRepo;
  private
    final UserService userService;

  public
    CheckoutController(CheckoutService checkoutService,
                       CartService cartService,
                       ProductRepository productRepo,
                       UserService userService) {
        this.checkoutService = checkoutService;
        this.cartService = cartService;
        this.productRepo = productRepo;
        this.userService = userService;
    }

  private
    Long currentUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        return userId == null ? 1L : (Long)userId;
    }

    @GetMapping("/checkout") public String form(Model model, HttpSession session) {
        Long userId = currentUserId(session);
        List<CartItem> items = cartService.getCart(userId);

        long subtotal = 0;
        long shippingFee = items.isEmpty() ? 0 : 30000;
        List<Map<String, Object>> lines = new ArrayList<>();

        for (CartItem ci : items) {
            var p = productRepo.findById(ci.getProductId()).orElse(null);
            if (p == null) {
                continue;
            }

            long lineTotal = p.getPrice() * ci.getQuantity();
            subtotal += lineTotal;

            Map<String, Object> line = new HashMap<>();
            line.put("productId", p.getId());
            line.put("name", p.getName());
            line.put("price", p.getPrice());
            line.put("qty", ci.getQuantity());
            line.put("lineTotal", lineTotal);
            lines.add(line);
        }

        long discountAmount = 0;
        long grandTotal = subtotal + shippingFee;
        var user = userService.getUserById(userId);

        model.addAttribute("address", user.getAddress() == null ? "" : user.getAddress());
        model.addAttribute("voucherCode", "");
        model.addAttribute("discountAmount", discountAmount);
        model.addAttribute("lines", lines);
        model.addAttribute("subtotal", subtotal);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("cartCount", items.size());
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("role", session.getAttribute("role"));
        return "checkout";
    }

    @PostMapping("/checkout") public String submit(@RequestParam String address,
                                                   @RequestParam(required = false) String voucherCode,
                                                   Model model,
                                                   HttpSession session) {
        try {
            var order = checkoutService.checkout(currentUserId(session), address, voucherCode);
            model.addAttribute("orderId", order.getId());
            model.addAttribute("total", order.getTotal());
            model.addAttribute("voucherCode", order.getVoucherCode());
            model.addAttribute("discountAmount", order.getDiscountAmount());
            return "checkout_success";
        } catch (ContractViolationException ex) {
            Long userId = currentUserId(session);
            List<CartItem> items = cartService.getCart(userId);

            long subtotal = 0;
            long shippingFee = items.isEmpty() ? 0 : 30000;
            List<Map<String, Object>> lines = new ArrayList<>();

            for (CartItem ci : items) {
                var p = productRepo.findById(ci.getProductId()).orElse(null);
                if (p == null) {
                    continue;
                }

                long lineTotal = p.getPrice() * ci.getQuantity();
                subtotal += lineTotal;

                Map<String, Object> line = new HashMap<>();
                line.put("productId", p.getId());
                line.put("name", p.getName());
                line.put("price", p.getPrice());
                line.put("qty", ci.getQuantity());
                line.put("lineTotal", lineTotal);
                lines.add(line);
            }

            long discountAmount = 0;
            long grandTotal = subtotal + shippingFee;

            model.addAttribute("error", ex.getMessage());
            model.addAttribute("address", address);
            model.addAttribute("voucherCode", voucherCode == null ? "" : voucherCode);
            model.addAttribute("discountAmount", discountAmount);
            model.addAttribute("lines", lines);
            model.addAttribute("subtotal", subtotal);
            model.addAttribute("shippingFee", shippingFee);
            model.addAttribute("grandTotal", grandTotal);
            model.addAttribute("cartCount", items.size());
            model.addAttribute("userEmail", session.getAttribute("userEmail"));
            model.addAttribute("role", session.getAttribute("role"));
            return "checkout";
        }
    }
}
