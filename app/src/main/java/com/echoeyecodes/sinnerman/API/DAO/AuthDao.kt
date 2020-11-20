package com.echoeyecodes.sinnerman.API.DAO

import com.echoeyecodes.sinnerman.Models.OtpModel
import com.echoeyecodes.sinnerman.Models.UserModel
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

    @POST("api/v1/otp/")
    suspend fun resendOtp(@Body otpModel : OtpModel) : ResponseBody
}
