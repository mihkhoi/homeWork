package com.example.shop.service;

import com.example.shop.entity.Voucher;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.VoucherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoucherServiceTest {

  private
    VoucherRepository voucherRepo;
  private
    VoucherService voucherService;

    @BeforeEach void setup() {
        voucherRepo = mock(VoucherRepository.class);
        voucherService = new VoucherService(voucherRepo);
    }

    @Test void calculateDiscount_blankCode_shouldReturnZero() {
        long discount = voucherService.calculateDiscount("", 200000);
        assertEquals(0, discount);
    }

    @Test void calculateDiscount_voucherNotFound_shouldThrow_PRE() {
        when(voucherRepo.findByCode("SALE10")).thenReturn(Optional.empty());

        assertThrows(ContractViolationException.class,
                     ()->voucherService.calculateDiscount("SALE10", 200000));
    }

    @Test void calculateDiscount_expiredVoucher_shouldThrow_PRE() {
        Voucher v = validVoucher();
        v.setEndAt(LocalDateTime.now().minusDays(1));

        when(voucherRepo.findByCode("SALE10")).thenReturn(Optional.of(v));

        assertThrows(ContractViolationException.class,
                     ()->voucherService.calculateDiscount("SALE10", 200000));
    }

    @Test void calculateDiscount_validPercentVoucher_shouldReturnCorrectDiscount() {
        Voucher v = validVoucher();
        when(voucherRepo.findByCode("SALE10")).thenReturn(Optional.of(v));

        long discount = voucherService.calculateDiscount("SALE10", 200000);

        assertEquals(20000, discount);
    }

    @Test void applyVoucher_valid_shouldIncreaseUsedCount() {
        Voucher v = validVoucher();
        v.setUsedCount(2);

        when(voucherRepo.findByCode("SALE10")).thenReturn(Optional.of(v));
        when(voucherRepo.save(any(Voucher.class))).thenAnswer(inv->inv.getArgument(0));

        Voucher saved = voucherService.applyVoucher("SALE10", 200000);

        assertEquals(3, saved.getUsedCount());
    }

  private
    Voucher validVoucher() {
        Voucher v = new Voucher();
        v.setCode("SALE10");
        v.setDiscountType("PERCENT");
        v.setDiscountValue(10);
        v.setMinOrderValue(100000);
        v.setMaxDiscount(50000);
        v.setUsageLimit(100);
        v.setUsedCount(0);
        v.setActive(true);
        v.setStartAt(LocalDateTime.now().minusDays(1));
        v.setEndAt(LocalDateTime.now().plusDays(1));
        return v;
    }
}
