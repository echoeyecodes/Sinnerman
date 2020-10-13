package com.example.myapplication.Models;

public class OtpModel {
    private String otp;
    private String email;
    private String token;

    public OtpModel(String otp, String email) {
        this.otp = otp;
        this.email = email;
    }

    public OtpModel(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
