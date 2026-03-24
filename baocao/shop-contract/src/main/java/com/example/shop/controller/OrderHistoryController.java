package com.example.shop.controller;

import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.service.OrderHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller public class OrderHistoryController {

  private
    final OrderRepository orderRepo;
  private
    final OrderItemRepository orderItemRepo;
  private
    final ProductRepository productRepo;
  private
    final OrderHistoryService orderHistoryService;

  public
    OrderHistoryController(OrderRepository orderRepo,
                           OrderItemRepository orderItemRepo,
                           ProductRepository productRepo,
                           OrderHistoryService orderHistoryService) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.productRepo = productRepo;
        this.orderHistoryService = orderHistoryService;
    }

  private
    Long currentUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        return userId == null ? 1L : (Long)userId;
    }

    @GetMapping("/orders") public String orders(Model model, HttpSession session) {
        var orders = orderRepo.findByUserIdOrderByCreatedAtDesc(currentUserId(session));
        model.addAttribute("orders", orders);
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("role", session.getAttribute("role"));
        return "orders";
    }

    @GetMapping("/orders/{id}") public String orderDetail(@PathVariable Long id, Model model, HttpSession session) {
        Long userId = currentUserId(session);

        var order = orderRepo.findById(id).orElse(null);
        if (order == null || !order.getUserId().equals(userId)) {
            model.addAttribute("message", "Không tìm thấy đơn hàng");
            return "order_not_found";
        }

        var items = orderItemRepo.findByOrderId(id);
        List<Map<String, Object>> lines = new ArrayList<>();

        for (var item : items) {
            var p = productRepo.findById(item.getProductId()).orElse(null);

            Map<String, Object> line = new HashMap<>();
            line.put("productId", item.getProductId());
            line.put("name", p != null ? p.getName() : "Sản phẩm không tồn tại");
            line.put("price", item.getPrice());
            line.put("qty", item.getQuantity());
            line.put("subtotal", item.getPrice() * item.getQuantity());
            lines.add(line);
        }

        model.addAttribute("order", order);
        model.addAttribute("lines", lines);
        model.addAttribute("canCancel", orderHistoryService.canCancel(order));
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("role", session.getAttribute("role"));
        return "order_detail";
    }

    @PostMapping("/orders/{id}/cancel") public String cancelOrder(@PathVariable Long id,
                                                                  HttpSession session,
                                                                  RedirectAttributes redirectAttributes) {
        try {
            orderHistoryService.cancelOrder(currentUserId(session), id);
            redirectAttributes.addFlashAttribute("success", "Đã hủy đơn hàng thành công.");
            return "redirect:/orders/" + id;
        } catch (ContractViolationException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
            return "redirect:/orders";
        }
    }
}
