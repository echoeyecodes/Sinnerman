package com.example.myapplication.Utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.Models.VideoResponseBody;
import com.example.myapplication.ViewModel.MainActivityViewModel;
import com.example.myapplication.ViewModel.NetworkState;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class VideoThreadHandler extends HandlerThread {

    private CustomHandler handler;
    private MainActivityViewModel mainActivityViewModel;
    private VideosDao videosDao;
    private static final int FETCH_HOME_MESSAGES = 0;

    public VideoThreadHandler(String name, MainActivityViewModel mainActivityViewModel, VideosDao videosDao) {
        super(name);
        this.mainActivityViewModel = mainActivityViewModel;
        this.videosDao = videosDao;
    }

    public MainActivityViewModel getMainActivityViewModel() {
        return mainActivityViewModel;
    }

    @Override
    protected void onLooperPrepared() {
        super.onLooperPrepared();
        handler = new CustomHandler(this);
    }

    public Handler getHandler() {
        return handler;
    }

    private void sendVideoList(List<VideoResponseBody> videos){
        getMainActivityViewModel().getIsLoadingHomeFragment().postValue(NetworkState.SUCCESS);
        getMainActivityViewModel().getVideos().postValue(videos);
    }

    public Response<List<VideoResponseBody>> fetchHomeVideos(String offset) throws IOException {
        Call<List<VideoResponseBody>> videos = videosDao.fetchHomeVideos("20", "0");
        return videos.execute();
    }

    private static class CustomHandler extends Handler{

        public VideoThreadHandler videoThreadHandler;

        public CustomHandler(VideoThreadHandler videoThreadHandler){
            this.videoThreadHandler = videoThreadHandler;
        }


        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case FETCH_HOME_MESSAGES:
                    String offset = (String) msg.obj;
                    try {
                        Response<List<VideoResponseBody>> response = videoThreadHandler.fetchHomeVideos(offset);
                        if(response.isSuccessful() && response.body() != null){
                            videoThreadHandler.sendVideoList(response.body());
                        }else{
                            videoThreadHandler.getMainActivityViewModel().setMessage("Could not fetch videos. Please try again");
                            videoThreadHandler.getMainActivityViewModel().getIsLoadingHomeFragment().postValue(NetworkState.ERROR);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        videoThreadHandler.getMainActivityViewModel().setMessage("Error connecting to server...");
                        videoThreadHandler.getMainActivityViewModel().getIsLoadingHomeFragment().postValue(NetworkState.ERROR);
                    }
            }
        }
    }
}
