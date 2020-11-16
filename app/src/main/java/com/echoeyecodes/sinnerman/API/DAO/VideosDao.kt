package com.echoeyecodes.sinnerman.API.DAO

import com.echoeyecodes.sinnerman.Models.ExploreResponseBody
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface VideosDao {

    @GET("api/v1/video")
    suspend fun fetchVideos(@Query("limit") limit : String, @Query("offset") offset : String, @Query("category") category: String) : List<VideoResponseBody>

    @GET("api/v1/video/activity/{context}")
    suspend fun fetchVideoActivity(@Path("context") context:String, @Query("limit") limit : String, @Query("offset") offset : String) : List<VideoResponseBody>

    @GET("api/v1/video/{id}")
     suspend fun fetchVideo(@Path("id") id : String) : VideoResponseBody

    @GET("api/v1/video/explore/tags")
    suspend fun fetchExplore(@Query("limit") limit : String, @Query("offset") offset : String, @Query("category")category: String) : List<ExploreResponseBody>

    @GET("api/v1/video/explore/tags/{id}")
    suspend fun fetchExploreItem(@Path("id") id:String, @Query("limit") limit : String, @Query("offset") offset : String, @Query("category") category: String) : List<VideoResponseBody>

}
