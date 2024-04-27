package com.example.furniturebuyandsell.model;

public class AuthResponse {

    //model này dùng để nhận các dữ liệu từ server
    private User thisCustomer;
    private Discount thisDiscount;

    private String otp;
    private String message;

    public AuthResponse() {
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getThisCustomer() {
        return thisCustomer;
    }

    public void setThisCustomer(User thisCustomer) {
        this.thisCustomer = thisCustomer;
    }

    public Discount getThisDiscount() {
        return thisDiscount;
    }

    public void setThisDiscount(Discount thisDiscount) {
        this.thisDiscount = thisDiscount;
    }
}
