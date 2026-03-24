package com.example.shop.dto;

public
class ProfileUpdateRequest {
  private
    String fullName;
  private
    String phone;
  private
    String address;

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
