package com.example.shop.service;

import com.example.shop.dto.ChangePasswordRequest;
import com.example.shop.dto.ProfileUpdateRequest;
import com.example.shop.entity.User;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service public class UserService {

  private
    final UserRepository userRepo;

  public
    UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

  public
    User getUserById(Long userId) {
        if (userId == null) {
            throw new ContractViolationException("PRE: userId null");
        }

        return userRepo.findById(userId)
            .orElseThrow(()->new ContractViolationException("PRE: user not found"));
    }

  public
    User updateProfile(Long userId, ProfileUpdateRequest req) {
        if (userId == null) {
            throw new ContractViolationException("PRE: userId null");
        }
        if (req == null) {
            throw new ContractViolationException("PRE: request null");
        }
        if (req.getFullName() == null || req.getFullName().isBlank()) {
            throw new ContractViolationException("PRE: fullName must not be blank");
        }
        if (req.getAddress() == null || req.getAddress().isBlank()) {
            throw new ContractViolationException("PRE: address must not be blank");
        }

        User user = userRepo.findById(userId)
                        .orElseThrow(()->new ContractViolationException("PRE: user not found"));

        if (!"USER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            throw new ContractViolationException("INV: invalid role");
        }

        user.setFullName(req.getFullName().trim());
        user.setPhone(req.getPhone() == null ? "" : req.getPhone().trim());
        user.setAddress(req.getAddress().trim());

        User saved = userRepo.save(user);

        if (!saved.getFullName().equals(req.getFullName().trim())) {
            throw new ContractViolationException("POST: fullName not updated");
        }
        if (!saved.getAddress().equals(req.getAddress().trim())) {
            throw new ContractViolationException("POST: address not updated");
        }

        return saved;
    }

  public
    void changePassword(Long userId, ChangePasswordRequest req) {
        if (userId == null) {
            throw new ContractViolationException("PRE: userId null");
        }
        if (req == null) {
            throw new ContractViolationException("PRE: request null");
        }
        if (req.getCurrentPassword() == null || req.getCurrentPassword().isBlank()) {
            throw new ContractViolationException("PRE: current password must not be blank");
        }
        if (req.getNewPassword() == null || req.getNewPassword().length() < 6) {
            throw new ContractViolationException("PRE: new password must be at least 6 chars");
        }
        if (req.getConfirmPassword() == null || !req.getConfirmPassword().equals(req.getNewPassword())) {
            throw new ContractViolationException("PRE: confirm password does not match");
        }

        User user = userRepo.findById(userId)
                        .orElseThrow(()->new ContractViolationException("PRE: user not found"));

        String currentHash = sha256(req.getCurrentPassword());
        if (!currentHash.equals(user.getPasswordHash())) {
            throw new ContractViolationException("PRE: current password is incorrect");
        }

        String newHash = sha256(req.getNewPassword());
        if (newHash.equals(user.getPasswordHash())) {
            throw new ContractViolationException("INV: new password must be different from current password");
        }

        user.setPasswordHash(newHash);
        User saved = userRepo.save(user);

        if (!saved.getPasswordHash().equals(newHash)) {
            throw new ContractViolationException("POST: password not updated");
        }
    }

  private
    String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hash error", e);
        }
    }
}
