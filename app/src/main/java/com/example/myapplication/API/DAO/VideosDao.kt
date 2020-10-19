package com.example.myapplication.API.DAO

import com.example.myapplication.Models.VideoModel
import com.example.myapplication.Models.VideoResponseBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideosDao {

    @GET("api/v1/video")
    suspend fun fetchVideos(@Query("limit") limit : String, @Query("offset") offset : String) : List<VideoResponseBody>

    @GET("api/v1/video/{id}")
     fun fetchVideo(@Path("id") id : String) : Call<VideoResponseBody>

}
