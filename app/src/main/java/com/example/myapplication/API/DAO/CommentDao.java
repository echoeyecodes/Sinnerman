package com.example.myapplication.API.DAO;

import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.CommentResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;
import java.util.UUID;

public interface CommentDao {

    @GET("api/v1/comment/{id}")
    Call<List<CommentResponseBody>> getComments(@Path("id") String id);

    @POST("api/v1/comment/")
    Call<CommentResponseBody> sendComment(@Body CommentModel commentModel);

}
