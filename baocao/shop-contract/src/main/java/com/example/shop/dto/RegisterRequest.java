package com.example.shop.dto;

public
class RegisterRequest {
  private
    String email;
  private
    String password;
  private
    String fullName;
  private
    String phone;
  private
    String address;

  public
    String getEmail() {
        return email;
    }
  public
    String getPassword() {
        return password;
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
    void setEmail(String email) {
        this.email = email;
    }
  public
    void setPassword(String password) {
        this.password = password;
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
}
