package com.example.shop.service;

import com.example.shop.dto.RegisterRequest;
import com.example.shop.entity.User;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterServiceTest {

  private
    UserRepository userRepo;
  private
    RegisterService registerService;

    @BeforeEach void setup() {
        userRepo = mock(UserRepository.class);
        registerService = new RegisterService(userRepo);
    }

  private
    RegisterRequest req(String email, String password, String fullName) {
        RegisterRequest r = new RegisterRequest();
        r.setEmail(email);
        r.setPassword(password);
        r.setFullName(fullName);
        r.setPhone("0900000000");
        r.setAddress("123 Test Street");
        return r;
    }

    @Test void register_requestNull_shouldThrow_PRE() {
        assertThrows(ContractViolationException.class,
                     ()->registerService.register(null));
    }

    @Test void register_emailNull_shouldThrow_PRE() {
        RegisterRequest r = req(null, "123456", "Nguyen Van A");

        assertThrows(ContractViolationException.class,
                     ()->registerService.register(r));
    }

    @Test void register_emailBlank_shouldThrow_PRE_conditionCoverage() {
        RegisterRequest r = req("   ", "123456", "Nguyen Van A");

        assertThrows(ContractViolationException.class,
                     ()->registerService.register(r));
    }

    @Test void register_emailInvalidFormat_shouldThrow_PRE() {
        RegisterRequest r = req("abc", "123456", "Nguyen Van A");

        assertThrows(ContractViolationException.class,
                     ()->registerService.register(r));
    }

    @Test void register_passwordTooShort_shouldThrow_PRE() {
        RegisterRequest r = req("a@gmail.com", "123", "Nguyen Van A");

        assertThrows(ContractViolationException.class,
                     ()->registerService.register(r));
    }

    @Test void register_fullNameBlank_shouldThrow_PRE() {
        RegisterRequest r = req("a@gmail.com", "123456", "   ");

        assertThrows(ContractViolationException.class,
                     ()->registerService.register(r));
    }

    @Test void register_duplicateEmail_shouldThrow_INV() {
        RegisterRequest r = req("a@gmail.com", "123456", "Nguyen Van A");

        when(userRepo.existsByEmail("a@gmail.com")).thenReturn(true);

        ContractViolationException ex = assertThrows(ContractViolationException.class,
                                                     ()->registerService.register(r));

        assertTrue(ex.getMessage().contains("INV"));
        verify(userRepo, times(1)).existsByEmail("a@gmail.com");
        verify(userRepo, never()).save(any());
    }

    @Test void register_valid_shouldSaveUser_andMeetPost() {
        RegisterRequest r = req("A@GMAIL.COM  ", "123456", "Nguyen Van A");

        when(userRepo.existsByEmail("a@gmail.com")).thenReturn(false);
        when(userRepo.count()).thenReturn(0L).thenReturn(1L);

        when(userRepo.save(any(User.class))).thenAnswer(invocation->{
            User u = invocation.getArgument(0);

            User saved = new User();
            saved.setEmail(u.getEmail());
            saved.setPasswordHash(u.getPasswordHash());
            saved.setRole(u.getRole());
            saved.setFullName(u.getFullName());
            saved.setPhone(u.getPhone());
            saved.setAddress(u.getAddress());
            saved.setStatus(u.getStatus());

            try {
                var f = User.class.getDeclaredField("id");
                f.setAccessible(true);
                f.set(saved, 1L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return saved;
        });

        User saved = registerService.register(r);

        assertNotNull(saved.getId());
        assertEquals("a@gmail.com", saved.getEmail());
        assertNotEquals("123456", saved.getPasswordHash());
        assertEquals("Nguyen Van A", saved.getFullName());
        assertEquals("ACTIVE", saved.getStatus());

        verify(userRepo).save(any(User.class));
    }
}
