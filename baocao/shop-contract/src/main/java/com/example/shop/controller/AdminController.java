package com.example.shop.controller;

import com.example.shop.dto.AdminProductRequest;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.OrderItemRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.ProductRepository;
import com.example.shop.repository.UserRepository;
import com.example.shop.service.AdminOrderService;
import com.example.shop.service.AdminProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
    @RequestMapping("/admin") public class AdminController {

  private
    final ProductRepository productRepo;
  private
    final OrderRepository orderRepo;
  private
    final OrderItemRepository orderItemRepo;
  private
    final UserRepository userRepo;
  private
    final AdminProductService adminProductService;
  private
    final AdminOrderService adminOrderService;

  public
    AdminController(ProductRepository productRepo,
                    OrderRepository orderRepo,
                    OrderItemRepository orderItemRepo,
                    UserRepository userRepo,
                    AdminProductService adminProductService,
                    AdminOrderService adminOrderService) {
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.userRepo = userRepo;
        this.adminProductService = adminProductService;
        this.adminOrderService = adminOrderService;
    }

  private
    void ensureAdmin(HttpSession session) {
        Object role = session.getAttribute("role");
        if (role == null || !"ADMIN".equals(role)) {
            throw new ContractViolationException("PRE: admin role required");
        }
    }

    @GetMapping public String dashboard(Model model, HttpSession session) {
        ensureAdmin(session);

        model.addAttribute("totalProducts", productRepo.count());
        model.addAttribute("totalOrders", orderRepo.count());
        model.addAttribute("totalCustomers", userRepo.findByRoleOrderByIdDesc("USER").size());
        model.addAttribute("pendingOrders", orderRepo.countByStatus("PENDING"));
        model.addAttribute("adminEmail", session.getAttribute("userEmail"));
        return "admin/admin_dashboard";
    }

    @GetMapping("/products") public String products(Model model, HttpSession session) {
        ensureAdmin(session);
        model.addAttribute("products", productRepo.findAll());
        model.addAttribute("adminEmail", session.getAttribute("userEmail"));
        return "admin/admin_products";
    }

    @GetMapping("/products/new") public String newProduct(Model model, HttpSession session) {
        ensureAdmin(session);
        model.addAttribute("form", new AdminProductRequest());
        model.addAttribute("mode", "create");
        return "admin/admin_product_form";
    }

    @PostMapping("/products/new") public String createProduct(@ModelAttribute("form") AdminProductRequest form,
                                                              Model model,
                                                              HttpSession session) {
        ensureAdmin(session);
        try {
            adminProductService.create(form);
            return "redirect:/admin/products";
        } catch (ContractViolationException ex) {
            model.addAttribute("form", form);
            model.addAttribute("mode", "create");
            model.addAttribute("error", ex.getMessage());
            return "admin/admin_product_form";
        }
    }

    @GetMapping("/products/{id}/edit") public String editProduct(@PathVariable Long id, Model model, HttpSession session) {
        ensureAdmin(session);

        var p = productRepo.findById(id)
                    .orElseThrow(()->new ContractViolationException("PRE: product not found"));

        AdminProductRequest form = new AdminProductRequest();
        form.setName(p.getName());
        form.setDescription(p.getDescription());
        form.setCategory(p.getCategory());
        form.setImageUrl(p.getImageUrl());
        form.setPrice(p.getPrice());
        form.setStock(p.getStock());
        form.setActive(p.isActive());

        model.addAttribute("productId", id);
        model.addAttribute("form", form);
        model.addAttribute("mode", "edit");
        return "admin/admin_product_form";
    }

    @PostMapping("/products/{id}/edit") public String updateProduct(@PathVariable Long id,
                                                                    @ModelAttribute("form") AdminProductRequest form,
                                                                    Model model,
                                                                    HttpSession session) {
        ensureAdmin(session);
        try {
            adminProductService.update(id, form);
            return "redirect:/admin/products";
        } catch (ContractViolationException ex) {
            model.addAttribute("productId", id);
            model.addAttribute("form", form);
            model.addAttribute("mode", "edit");
            model.addAttribute("error", ex.getMessage());
            return "admin/admin_product_form";
        }
    }

    @PostMapping("/products/{id}/toggle") public String toggleProduct(@PathVariable Long id, HttpSession session) {
        ensureAdmin(session);
        adminProductService.toggleActive(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/orders") public String orders(Model model, HttpSession session) {
        ensureAdmin(session);
        model.addAttribute("orders", orderRepo.findAllByOrderByCreatedAtDesc());
        model.addAttribute("adminEmail", session.getAttribute("userEmail"));
        return "admin/admin_orders";
    }

    @GetMapping("/orders/{id}") public String orderDetail(@PathVariable Long id, Model model, HttpSession session) {
        ensureAdmin(session);

        var order = orderRepo.findById(id)
                        .orElseThrow(()->new ContractViolationException("PRE: order not found"));

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
        return "admin/admin_order_detail";
    }

    @PostMapping("/orders/{id}/status") public String updateOrderStatus(@PathVariable Long id,
                                                                        @RequestParam String status,
                                                                        HttpSession session) {
        ensureAdmin(session);
        adminOrderService.updateStatus(id, status);
        return "redirect:/admin/orders/" + id;
    }

    @GetMapping("/customers") public String customers(Model model, HttpSession session) {
        ensureAdmin(session);
        model.addAttribute("customers", userRepo.findByRoleOrderByIdDesc("USER"));
        return "admin/admin_customers";
    }
}
