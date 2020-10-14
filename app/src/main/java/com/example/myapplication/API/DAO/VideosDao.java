package com.example.myapplication.API.DAO;

import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.Models.VideoResponseBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface VideosDao {

    @GET("api/v1/video")
    Call<List<VideoResponseBody>> fetchVideos(@Query("limit") String limit, @Query("offset") String offset);

}
