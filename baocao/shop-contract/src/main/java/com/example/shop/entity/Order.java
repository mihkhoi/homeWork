package com.example.shop.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
    @Table(name = "orders") public class Order {
    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @Column(name = "user_id", nullable = false) private Long userId;

    @Column(nullable = false, length = 500) private String address;

    @Column(nullable = false) private long total;

    @Column(name = "shipping_fee", nullable = false) private long shippingFee = 30000;

    @Column(name = "payment_method", nullable = false) private String paymentMethod = "COD";

    @Column(nullable = false) private String status = "PENDING";

    @Column(name = "voucher_code") private String voucherCode;

    @Column(name = "discount_amount", nullable = false) private long discountAmount;

    @Column(name = "created_at", nullable = false) private LocalDateTime createdAt;

  public
    Long getId() {
        return id;
    }

  public
    Long getUserId() {
        return userId;
    }

  public
    String getAddress() {
        return address;
    }

  public
    long getTotal() {
        return total;
    }

  public
    long getShippingFee() {
        return shippingFee;
    }

  public
    String getPaymentMethod() {
        return paymentMethod;
    }

  public
    String getStatus() {
        return status;
    }

  public
    String getVoucherCode() {
        return voucherCode;
    }

  public
    long getDiscountAmount() {
        return discountAmount;
    }

  public
    LocalDateTime getCreatedAt() {
        return createdAt;
    }

  public
    void setUserId(Long userId) {
        this.userId = userId;
    }

  public
    void setAddress(String address) {
        this.address = address;
    }

  public
    void setTotal(long total) {
        this.total = total;
    }

  public
    void setShippingFee(long shippingFee) {
        this.shippingFee = shippingFee;
    }

  public
    void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

  public
    void setStatus(String status) {
        this.status = status;
    }

  public
    void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

  public
    void setDiscountAmount(long discountAmount) {
        this.discountAmount = discountAmount;
    }

  public
    void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
