package com.example.shop.entity;

import jakarta.persistence.*;

@Entity
    @Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(columnNames = "email")) public class User {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

    @Column(nullable = false) private String email;

    @Column(nullable = false) private String passwordHash;

    @Column(nullable = false) private String role = "USER";

    @Column(nullable = false) private String fullName = "";

    @Column(nullable = false) private String phone = "";

    @Column(nullable = false, length = 500) private String address = "";

    @Column(nullable = false) private String status = "ACTIVE";

  public
    Long getId() {
        return id;
    }
  public
    String getEmail() {
        return email;
    }
  public
    String getPasswordHash() {
        return passwordHash;
    }
  public
    String getRole() {
        return role;
    }
  public
    String getFullName() {
        return fullName;
    }
  public
    String getPhone() {
        return phone;
    }
  public
    String getAddress() {
        return address;
    }
  public
    String getStatus() {
        return status;
    }

  public
    void setEmail(String email) {
        this.email = email;
    }
  public
    void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
  public
    void setRole(String role) {
        this.role = role;
    }
  public
    void setFullName(String fullName) {
        this.fullName = fullName;
    }
  public
    void setPhone(String phone) {
        this.phone = phone;
    }
  public
    void setAddress(String address) {
        this.address = address;
    }
  public
    void setStatus(String status) {
        this.status = status;
    }
}
