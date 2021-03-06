package com.echoeyecodes.sinnerman.API.DAO

import com.echoeyecodes.sinnerman.Models.UserModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserDao {

    @GET("api/v1/user/")
    fun getCurrentUser(): Call<UserModel>

    @Multipart
    @POST("api/v1/user/upload")
    suspend fun uploadPhoto(@Part image : MultipartBody.Part)


}