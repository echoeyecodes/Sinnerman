package com.example.myapplication.ViewModel;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.API.DAO.ApiClient;
import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Models.VideoResponseBody;
import com.example.myapplication.ViewModel.BottomFragmentViewModel.HomeFragmentViewModel;
import com.example.myapplication.util.AppHandlerThread;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VideoActivityViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isFullScreen = new MutableLiveData<>();
    private MutableLiveData<NetworkState> request_status = new MutableLiveData<>(NetworkState.LOADING);
    private MutableLiveData<VideoResponseBody> videoObserver = new MutableLiveData<>();
    private VideoResponseBody video = null;
    private HandlerThread handlerThread;
    private VideoActivityThreadCustomHandler customHandler;
    private final VideosDao videosDao;
    private ApiClient apiClient;
    private String video_id;
    private String message;

    public VideoActivityViewModel(Application application){
        super(application);
        isFullScreen.setValue(false);

        apiClient = new ApiClient(application);
        videosDao = apiClient.getClient(VideosDao.class);

        if(handlerThread == null){
            AppHandlerThread appHandlerThread = AppHandlerThread.getInstance();
            handlerThread = appHandlerThread.getHandlerThread();
        }
        customHandler = new VideoActivityThreadCustomHandler(handlerThread.getLooper(), videosDao, this);
    }

    public MutableLiveData<Boolean> getIsFullScreen() {
        return isFullScreen;
    }

    public boolean getIsFullScreenValue(){
        assert isFullScreen.getValue() != null;
        return isFullScreen.getValue();
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
        fetchVideo();
    }

    public void fetchVideo(){
            Message message = Message.obtain(customHandler);
            message.obj = video_id;
            message.sendToTarget();
    }

    public MutableLiveData<NetworkState> getRequest_status() {
        return request_status;
    }

    public MutableLiveData<VideoResponseBody> getVideoObserver() {
        return videoObserver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setVideo(VideoResponseBody new_video) {
        request_status.postValue(NetworkState.SUCCESS);
        video = new_video;
        videoObserver.postValue(video);
    }

    public void toggleFullScreen(boolean value){
        isFullScreen.setValue(value);
    }

    private static class VideoActivityThreadCustomHandler extends Handler {

        private VideoActivityViewModel videoActivityViewModel;
        private VideosDao videosDao;

        public VideoActivityThreadCustomHandler(Looper looper, VideosDao videosDao, VideoActivityViewModel videoActivityViewModel) {
            super(looper);
            this.videoActivityViewModel = videoActivityViewModel;
            this.videosDao = videosDao;
        }

        public void fetchVideo(String id){
            Call<VideoResponseBody> call =  videosDao.fetchVideo(id);
            try {
                Response<VideoResponseBody> response = call.execute();
                if(response.isSuccessful() && response.body() != null){
                    videoActivityViewModel.setVideo(response.body());
                }else{
                    videoActivityViewModel.getRequest_status().postValue(NetworkState.ERROR);
                    videoActivityViewModel.setMessage("An error occurred while retrieving videos");
                }
            } catch (IOException e) {
                e.printStackTrace();
                videoActivityViewModel.getRequest_status().postValue(NetworkState.ERROR);
                videoActivityViewModel.setMessage("Couldn't connect to server. Please try again");
            }
        }
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);
            fetchVideo((String) msg.obj);
        }
    }
}
