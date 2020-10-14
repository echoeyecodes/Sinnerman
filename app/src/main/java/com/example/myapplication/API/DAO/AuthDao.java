package com.example.myapplication.API.DAO;

import com.example.myapplication.Models.OtpModel;
import com.example.myapplication.Models.UserModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthDao {

    @POST("api/v1/auth/signup")
    Call<String> createUser(@Body UserModel userModel);

    @POST("api/v1/auth/login")
    Call<String> login(@Body UserModel userModel);

    @POST("api/v1/otp/verify")
    Call<ResponseBody> verify(@Body OtpModel otpModel);
}
