package com.example.shop.service;

import com.example.shop.dto.RegisterRequest;
import com.example.shop.entity.User;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service public class RegisterService {

  private
    final UserRepository userRepo;

  public
    RegisterService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

  public
    User register(RegisterRequest req) {
        if (req == null) {
            throw new ContractViolationException("PRE: request null");
        }
        if (req.getEmail() == null || req.getEmail().isBlank()) {
            throw new ContractViolationException("PRE: email must not be blank");
        }

        String normalizedEmail = req.getEmail().trim().toLowerCase();

        if (!normalizedEmail.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            throw new ContractViolationException("PRE: email format invalid");
        }
        if (req.getPassword() == null || req.getPassword().length() < 6) {
            throw new ContractViolationException("PRE: password must be at least 6 chars");
        }
        if (req.getFullName() == null || req.getFullName().isBlank()) {
            throw new ContractViolationException("PRE: fullName must not be blank");
        }
        if (userRepo.existsByEmail(normalizedEmail)) {
            throw new ContractViolationException("INV: email already exists");
        }

        long beforeCount = userRepo.count();

        User u = new User();
        u.setEmail(normalizedEmail);
        u.setPasswordHash(sha256(req.getPassword()));
        u.setRole("USER");
        u.setFullName(req.getFullName().trim());
        u.setPhone(req.getPhone() == null ? "" : req.getPhone().trim());
        u.setAddress(req.getAddress() == null ? "" : req.getAddress().trim());
        u.setStatus("ACTIVE");

        User saved = userRepo.save(u);

        if (saved.getId() == null) {
            throw new ContractViolationException("POST: user id must be generated");
        }
        if (!saved.getEmail().equals(normalizedEmail)) {
            throw new ContractViolationException("POST: email must be normalized");
        }
        if (userRepo.count() != beforeCount + 1) {
            throw new ContractViolationException("POST: user count must increase by 1");
        }

        return saved;
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
