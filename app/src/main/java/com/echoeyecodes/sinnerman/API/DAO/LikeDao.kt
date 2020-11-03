package com.echoeyecodes.sinnerman.API.DAO


import com.echoeyecodes.sinnerman.Models.LikeModel
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface LikeDao {

    @POST("api/v1/like/")
    suspend fun sendLike(@Query ("type") type:String, @Body like: LikeModel): VideoResponseBody

}