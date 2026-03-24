package com.example.shop.service;

import com.example.shop.entity.Voucher;
import com.example.shop.exception.ContractViolationException;
import com.example.shop.repository.VoucherRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service public class VoucherService {

  private
    final VoucherRepository voucherRepo;

  public
    VoucherService(VoucherRepository voucherRepo) {
        this.voucherRepo = voucherRepo;
    }

  public
    long calculateDiscount(String code, long subtotal) {
        if (code == null || code.isBlank()) {
            return 0;
        }

        Voucher voucher = voucherRepo.findByCode(normalize(code))
                              .orElseThrow(()->new ContractViolationException("PRE: voucher not found"));

        validateVoucher(voucher, subtotal);

        long discount;
        if ("PERCENT".equalsIgnoreCase(voucher.getDiscountType())) {
            discount = subtotal * voucher.getDiscountValue() / 100;
            if (voucher.getMaxDiscount() > 0 && discount > voucher.getMaxDiscount()) {
                discount = voucher.getMaxDiscount();
            }
        } else if ("FIXED".equalsIgnoreCase(voucher.getDiscountType())) {
            discount = voucher.getDiscountValue();
        } else {
            throw new ContractViolationException("INV: invalid discount type");
        }

        if (discount < 0) {
            throw new ContractViolationException("INV: discount must be >= 0");
        }
        if (discount > subtotal) {
            discount = subtotal;
        }

        return discount;
    }

  public
    Voucher applyVoucher(String code, long subtotal) {
        if (code == null || code.isBlank()) {
            return null;
        }

        Voucher voucher = voucherRepo.findByCode(normalize(code))
                              .orElseThrow(()->new ContractViolationException("PRE: voucher not found"));

        validateVoucher(voucher, subtotal);

        int before = voucher.getUsedCount();
        voucher.setUsedCount(before + 1);
        Voucher saved = voucherRepo.save(voucher);

        if (saved.getUsedCount() != before + 1) {
            throw new ContractViolationException("POST: usedCount must increase by 1");
        }

        return saved;
    }

  private
    void validateVoucher(Voucher voucher, long subtotal) {
        if (!voucher.isActive()) {
            throw new ContractViolationException("PRE: voucher inactive");
        }

        LocalDateTime now = LocalDateTime.now();
        if (voucher.getStartAt() != null && now.isBefore(voucher.getStartAt())) {
            throw new ContractViolationException("PRE: voucher not started");
        }
        if (voucher.getEndAt() != null && now.isAfter(voucher.getEndAt())) {
            throw new ContractViolationException("PRE: voucher expired");
        }
        if (subtotal < voucher.getMinOrderValue()) {
            throw new ContractViolationException("PRE: subtotal does not meet minimum order value");
        }
        if (voucher.getUsageLimit() > 0 && voucher.getUsedCount() >= voucher.getUsageLimit()) {
            throw new ContractViolationException("PRE: voucher usage limit exceeded");
        }

        if (voucher.getDiscountValue() < 0) {
            throw new ContractViolationException("INV: discountValue must be >= 0");
        }
        if (voucher.getMinOrderValue() < 0) {
            throw new ContractViolationException("INV: minOrderValue must be >= 0");
        }
        if (voucher.getMaxDiscount() < 0) {
            throw new ContractViolationException("INV: maxDiscount must be >= 0");
        }
    }

  private
    String normalize(String code) {
        return code.trim().toUpperCase();
    }
}
