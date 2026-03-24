package com.example.shop.controller;

import com.example.shop.dto.ChangePasswordRequest;
import com.example.shop.dto.ProfileUpdateRequest;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller public class ProfileController {

  private
    final UserService userService;

  public
    ProfileController(UserService userService) {
        this.userService = userService;
    }

  private
    Long currentUserId(HttpSession session) {
        Object userId = session.getAttribute("userId");
        return userId == null ? 1L : (Long)userId;
    }

  private
    void loadProfilePage(Model model, HttpSession session, ProfileUpdateRequest profileForm, ChangePasswordRequest passwordForm) {
        var user = userService.getUserById(currentUserId(session));

        model.addAttribute("user", user);
        model.addAttribute("form", profileForm);
        model.addAttribute("passwordForm", passwordForm);
        model.addAttribute("userEmail", session.getAttribute("userEmail"));
        model.addAttribute("role", session.getAttribute("role"));
    }

    @GetMapping("/profile") public String profile(Model model, HttpSession session) {
        var user = userService.getUserById(currentUserId(session));

        ProfileUpdateRequest form = new ProfileUpdateRequest();
        form.setFullName(user.getFullName());
        form.setPhone(user.getPhone());
        form.setAddress(user.getAddress());

        ChangePasswordRequest passwordForm = new ChangePasswordRequest();

        loadProfilePage(model, session, form, passwordForm);
        return "profile";
    }

    @PostMapping("/profile") public String updateProfile(@ModelAttribute("form") ProfileUpdateRequest form,
                                                         Model model,
                                                         HttpSession session) {
        try {
            var saved = userService.updateProfile(currentUserId(session), form);
            session.setAttribute("fullName", saved.getFullName());

            ChangePasswordRequest passwordForm = new ChangePasswordRequest();
            loadProfilePage(model, session, form, passwordForm);
            model.addAttribute("success", "Cập nhật hồ sơ thành công");
            return "profile";
        } catch (ContractViolationException ex) {
            ChangePasswordRequest passwordForm = new ChangePasswordRequest();
            loadProfilePage(model, session, form, passwordForm);
            model.addAttribute("error", ex.getMessage());
            return "profile";
        }
    }

    @PostMapping("/profile/change-password") public String changePassword(@ModelAttribute("passwordForm") ChangePasswordRequest passwordForm,
                                                                          Model model,
                                                                          HttpSession session) {
        var user = userService.getUserById(currentUserId(session));

        ProfileUpdateRequest form = new ProfileUpdateRequest();
        form.setFullName(user.getFullName());
        form.setPhone(user.getPhone());
        form.setAddress(user.getAddress());

        try {
            userService.changePassword(currentUserId(session), passwordForm);
            loadProfilePage(model, session, form, new ChangePasswordRequest());
            model.addAttribute("passwordSuccess", "Đổi mật khẩu thành công");
            return "profile";
        } catch (ContractViolationException ex) {
            loadProfilePage(model, session, form, passwordForm);
            model.addAttribute("passwordError", ex.getMessage());
            return "profile";
        }
    }
}
