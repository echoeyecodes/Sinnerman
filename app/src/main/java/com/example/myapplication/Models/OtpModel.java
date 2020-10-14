package com.example.myapplication.Models;

public class OtpModel {
    private String otp;
    private String verification_response;
    private String token;

    public OtpModel(String otp, String verification_response) {
        this.otp = otp;
        this.verification_response = verification_response;
    }

    public OtpModel(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
