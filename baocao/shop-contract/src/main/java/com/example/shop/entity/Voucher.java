package com.example.shop.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
    @Table(name = "vouchers") public class Voucher {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @Column(nullable = false, unique = true, length = 50) private String code;

    @Column(name = "discount_type", nullable = false, length = 20) private String discountType; // PERCENT or FIXED

    @Column(name = "discount_value", nullable = false) private long discountValue;

    @Column(name = "min_order_value", nullable = false) private long minOrderValue;

    @Column(name = "max_discount", nullable = false) private long maxDiscount;

    @Column(name = "usage_limit", nullable = false) private int usageLimit;

    @Column(name = "used_count", nullable = false) private int usedCount;

    @Column(nullable = false) private boolean active;

    @Column(name = "start_at") private LocalDateTime startAt;

    @Column(name = "end_at") private LocalDateTime endAt;

  public
    Long getId() {
        return id;
    }

  public
    String getCode() {
        return code;
    }

  public
    void setCode(String code) {
        this.code = code;
    }

  public
    String getDiscountType() {
        return discountType;
    }

  public
    void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

  public
    long getDiscountValue() {
        return discountValue;
    }

  public
    void setDiscountValue(long discountValue) {
        this.discountValue = discountValue;
    }

  public
    long getMinOrderValue() {
        return minOrderValue;
    }

  public
    void setMinOrderValue(long minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

  public
    long getMaxDiscount() {
        return maxDiscount;
    }

  public
    void setMaxDiscount(long maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

  public
    int getUsageLimit() {
        return usageLimit;
    }

  public
    void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

  public
    int getUsedCount() {
        return usedCount;
    }

  public
    void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

  public
    boolean isActive() {
        return active;
    }

  public
    void setActive(boolean active) {
        this.active = active;
    }

  public
    LocalDateTime getStartAt() {
        return startAt;
    }

  public
    void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

  public
    LocalDateTime getEndAt() {
        return endAt;
    }

  public
    void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }
}
