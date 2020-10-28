package com.example.myapplication.API.DAO

import com.example.myapplication.Models.CommentModel
import com.example.myapplication.Models.CommentResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CommentDao {
    @GET("api/v1/comment/{id}")
    suspend fun getComments(@Path("id") id: String, @Query("limit") limit:String, @Query("offset") offset:String): List<CommentResponseBody>

    @POST("api/v1/comment/")
    suspend fun sendComment(@Body commentModel: CommentModel): CommentResponseBody
}