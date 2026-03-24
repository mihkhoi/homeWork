package com.example.shop.service;

import com.example.shop.dto.ChangePasswordRequest;
import com.example.shop.dto.ProfileUpdateRequest;
import com.example.shop.entity.User;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

  private
    UserRepository userRepo;
  private
    UserService userService;

    @BeforeEach void setup() {
        userRepo = mock(UserRepository.class);
        userService = new UserService(userRepo);
    }

  private
    ProfileUpdateRequest validReq() {
        ProfileUpdateRequest req = new ProfileUpdateRequest();
        req.setFullName("Nguyen Van A");
        req.setPhone("0900000000");
        req.setAddress("123 Le Loi");
        return req;
    }

  private
    ChangePasswordRequest validPasswordReq() {
        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setCurrentPassword("123456");
        req.setNewPassword("654321");
        req.setConfirmPassword("654321");
        return req;
    }

    @Test void updateProfile_userIdNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->userService.updateProfile(null, validReq()));
    }

    @Test void updateProfile_fullNameBlank_shouldThrow_PRE() {
        ProfileUpdateRequest req = validReq();
        req.setFullName("   ");

        assertThrows(ContractViolationException.class,
                     ()->userService.updateProfile(1L, req));
    }

    @Test void updateProfile_addressBlank_shouldThrow_PRE() {
        ProfileUpdateRequest req = validReq();
        req.setAddress("  ");

        assertThrows(ContractViolationException.class,
                     ()->userService.updateProfile(1L, req));
    }

    @Test void updateProfile_userNotFound_shouldThrow_PRE() {
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ContractViolationException.class,
                     ()->userService.updateProfile(1L, validReq()));
    }

    @Test void updateProfile_valid_shouldUpdateSuccessfully() {
        User u = new User();
        u.setEmail("a@gmail.com");
        u.setRole("USER");
        u.setStatus("ACTIVE");
        u.setFullName("Old Name");
        u.setPhone("111");
        u.setAddress("Old Address");

        when(userRepo.findById(1L)).thenReturn(Optional.of(u));
        when(userRepo.save(any(User.class))).thenAnswer(inv->inv.getArgument(0));

        ProfileUpdateRequest req = validReq();
        User saved = userService.updateProfile(1L, req);

        assertEquals("Nguyen Van A", saved.getFullName());
        assertEquals("0900000000", saved.getPhone());
        assertEquals("123 Le Loi", saved.getAddress());
        verify(userRepo).save(u);
    }

    @Test void changePassword_currentBlank_shouldThrow_PRE() {
        ChangePasswordRequest req = validPasswordReq();
        req.setCurrentPassword(" ");

        assertThrows(ContractViolationException.class,
                     ()->userService.changePassword(1L, req));
    }

    @Test void changePassword_newTooShort_shouldThrow_PRE() {
        ChangePasswordRequest req = validPasswordReq();
        req.setNewPassword("123");
        req.setConfirmPassword("123");

        assertThrows(ContractViolationException.class,
                     ()->userService.changePassword(1L, req));
    }

    @Test void changePassword_confirmMismatch_shouldThrow_PRE() {
        ChangePasswordRequest req = validPasswordReq();
        req.setConfirmPassword("xxxxxx");

        assertThrows(ContractViolationException.class,
                     ()->userService.changePassword(1L, req));
    }

    @Test void changePassword_wrongCurrentPassword_shouldThrow_PRE() throws Exception {
        User user = new User();
        user.setRole("USER");
        user.setPasswordHash(sha256("abcdef"));

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        ChangePasswordRequest req = validPasswordReq();

        assertThrows(ContractViolationException.class,
                     ()->userService.changePassword(1L, req));
    }

    @Test void changePassword_sameAsCurrent_shouldThrow_INV() throws Exception {
        User user = new User();
        user.setRole("USER");
        user.setPasswordHash(sha256("123456"));

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));

        ChangePasswordRequest req = new ChangePasswordRequest();
        req.setCurrentPassword("123456");
        req.setNewPassword("123456");
        req.setConfirmPassword("123456");

        assertThrows(ContractViolationException.class,
                     ()->userService.changePassword(1L, req));
    }

    @Test void changePassword_valid_shouldUpdatePassword() throws Exception {
        User user = new User();
        user.setRole("USER");
        user.setPasswordHash(sha256("123456"));

        when(userRepo.findById(1L)).thenReturn(Optional.of(user));
        when(userRepo.save(any(User.class))).thenAnswer(inv->inv.getArgument(0));

        ChangePasswordRequest req = validPasswordReq();

        userService.changePassword(1L, req);

        assertEquals(sha256("654321"), user.getPasswordHash());
        verify(userRepo).save(user);
    }

  private
    String sha256(String input) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
