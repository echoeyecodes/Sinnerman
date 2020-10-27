package com.example.myapplication.API.DAO

import com.example.myapplication.Models.UploadNotificationModel
import retrofit2.http.GET
import retrofit2.http.Query

interface UploadNotificationDao {

    @GET("api/v1/upload-notification")
    suspend fun fetchUploadNotifications(@Query("limit") limit : String, @Query("offset") offset : String) : List<UploadNotificationModel>
}