package com.example.myapplication.Controllers;

import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Models.VideoResponseBody;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class VideoController {
    private VideosDao videosDao;

    public VideoController(VideosDao videosDao){
        this.videosDao = videosDao;
    }

    private Response<List<VideoResponseBody>> fetchVideos(String limit, String offset) throws IOException {
        return videosDao.fetchVideos(limit, offset).execute();
    }

    private void fetchVideo(String id){

    }
}
