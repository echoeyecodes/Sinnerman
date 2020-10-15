package com.example.myapplication.API.DAO;


import com.example.myapplication.Models.UserModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface UserDao {

    @GET("api/v1/user/")
    Call<UserModel> getCurrentUser();
}
