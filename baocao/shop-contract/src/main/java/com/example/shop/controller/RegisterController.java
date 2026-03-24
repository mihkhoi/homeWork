package com.example.shop.controller;

import com.example.shop.dto.RegisterRequest;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.service.RegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller public class RegisterController {

  private
    final RegisterService registerService;

  public
    RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/register") public String showForm(Model model) {
        model.addAttribute("form", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register") public String submit(@ModelAttribute("form") RegisterRequest form, Model model) {
        try {
            registerService.register(form);
            model.addAttribute("message", "Đăng ký thành công!");
            return "register_success";
        } catch (ContractViolationException ex) {
            model.addAttribute("form", form);
            model.addAttribute("error", ex.getMessage());
            return "register";
        }
    }
}
