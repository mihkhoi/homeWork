package com.example.shop.controller;

import com.example.shop.dto.LoginRequest;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller public class LoginController {

  private
    final LoginService loginService;

  public
    LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login") public String showLoginForm(Model model) {
        model.addAttribute("form", new LoginRequest());
        return "login";
    }

    @PostMapping("/login") public String submit(@ModelAttribute("form") LoginRequest form,
                                                Model model,
                                                HttpSession session) {
        try {
            var user = loginService.login(form.getEmail(), form.getPassword());
            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("role", user.getRole());
            session.setAttribute("fullName", user.getFullName());

            if ("ADMIN".equals(user.getRole())) {
                return "redirect:/admin";
            }
            return "redirect:/products";
        } catch (ContractViolationException ex) {
            model.addAttribute("form", form);
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout") public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
