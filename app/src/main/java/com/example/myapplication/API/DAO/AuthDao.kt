package com.example.myapplication.API.DAO

import com.example.myapplication.Models.OtpModel
import com.example.myapplication.Models.UserModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthDao {

    @POST("api/v1/auth/signup")
    fun createUser(@Body userModel : UserModel) : Call<ResponseBody>

    @POST("api/v1/auth/login")
    fun login(@Body userModel : UserModel) : Call<ResponseBody>

    @POST("api/v1/otp/verify")
    fun verify(@Body otpModel : OtpModel) : Call<ResponseBody>
}
