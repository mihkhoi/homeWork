package com.example.shop.service;

import com.example.shop.entity.User;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service public class LoginService {

  private
    final UserRepository userRepo;

  public
    LoginService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

  public
    User login(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new ContractViolationException("PRE: email must not be blank");
        }
        if (password == null || password.isBlank()) {
            throw new ContractViolationException("PRE: password must not be blank");
        }

        String normalizedEmail = email.trim().toLowerCase();
        User user = userRepo.findByEmail(normalizedEmail)
                        .orElseThrow(()->new ContractViolationException("PRE: account not found"));

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new ContractViolationException("INV: account is inactive");
        }

        String hashed = sha256(password);
        if (!user.getPasswordHash().equals(hashed)) {
            throw new ContractViolationException("PRE: wrong password");
        }

        if (user.getId() == null) {
            throw new ContractViolationException("POST: login user must have id");
        }

        return user;
    }

  private
    String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash)
                sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hash error", e);
        }
    }
}
