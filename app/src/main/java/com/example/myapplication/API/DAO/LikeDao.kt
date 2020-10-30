package com.example.myapplication.API.DAO


import com.example.myapplication.Models.LikeModel
import com.example.myapplication.Models.VideoResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LikeDao {

    @POST("api/v1/like/")
    suspend fun sendLike(@Query ("type") type:String, @Body like: LikeModel): VideoResponseBody

}