package com.example.shop.service;

import com.example.shop.entity.User;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginServiceTest {

  private
    UserRepository userRepo;
  private
    LoginService loginService;

    @BeforeEach void setup() {
        userRepo = mock(UserRepository.class);
        loginService = new LoginService(userRepo);
    }

    @Test void login_emailBlank_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->loginService.login("   ", "123456"));
    }

    @Test void login_passwordBlank_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->loginService.login("a@gmail.com", " "));
    }

    @Test void login_accountNotFound_shouldThrow_PRE() {
        when(userRepo.findByEmail("a@gmail.com")).thenReturn(Optional.empty());

        assertThrows(ContractViolationException.class,
                     ()->loginService.login("a@gmail.com", "123456"));
    }

    @Test void login_inactiveAccount_shouldThrow_INV() throws Exception {
        User u = new User();
        u.setEmail("a@gmail.com");
        u.setPasswordHash(sha256("123456"));
        u.setStatus("INACTIVE");

        Field f = User.class.getDeclaredField("id");
        f.setAccessible(true);
        f.set(u, 1L);

        when(userRepo.findByEmail("a@gmail.com")).thenReturn(Optional.of(u));

        assertThrows(ContractViolationException.class,
                     ()->loginService.login("a@gmail.com", "123456"));
    }

    @Test void login_wrongPassword_shouldThrow_PRE() throws Exception {
        User u = new User();
        u.setEmail("a@gmail.com");
        u.setPasswordHash(sha256("correct123"));
        u.setStatus("ACTIVE");

        Field f = User.class.getDeclaredField("id");
        f.setAccessible(true);
        f.set(u, 1L);

        when(userRepo.findByEmail("a@gmail.com")).thenReturn(Optional.of(u));

        assertThrows(ContractViolationException.class,
                     ()->loginService.login("a@gmail.com", "wrong123"));
    }

    @Test void login_valid_shouldReturnUser() throws Exception {
        User u = new User();
        u.setEmail("a@gmail.com");
        u.setPasswordHash(sha256("123456"));
        u.setStatus("ACTIVE");
        u.setFullName("Nguyen Van A");
        u.setRole("USER");

        Field f = User.class.getDeclaredField("id");
        f.setAccessible(true);
        f.set(u, 1L);

        when(userRepo.findByEmail("a@gmail.com")).thenReturn(Optional.of(u));

        User result = loginService.login("A@GMAIL.COM", "123456");

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("a@gmail.com", result.getEmail());
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
