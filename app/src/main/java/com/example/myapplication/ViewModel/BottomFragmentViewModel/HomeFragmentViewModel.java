package com.example.myapplication.ViewModel.BottomFragmentViewModel;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.API.DAO.ApiClient;
import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Models.VideoResponseBody;
import com.example.myapplication.ViewModel.NetworkState;
import com.example.myapplication.util.AppHandlerThread;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragmentViewModel extends AndroidViewModel {
    private MutableLiveData<NetworkState> request_status = new MutableLiveData<>(NetworkState.LOADING);
    private MutableLiveData<List<VideoResponseBody>> videosObserver = new MutableLiveData<>();
    private List<VideoResponseBody> videos = new ArrayList<>();
    private HandlerThread handlerThread;
    private AppHandlerThread appHandlerThread = AppHandlerThread.getInstance();
    private HomeFragmentThreadCustomHandler customHandler;
    private final VideosDao videosDao;
    private ApiClient apiClient;
    private String message;



    public HomeFragmentViewModel(Application application){
        super(application);
        request_status.setValue(NetworkState.LOADING);
        apiClient = new ApiClient(application);
        videosDao = apiClient.getClient(VideosDao.class);

        if(handlerThread == null){
            handlerThread = appHandlerThread.getHandlerThread();
        }
        customHandler = new HomeFragmentThreadCustomHandler(handlerThread.getLooper(), videosDao, this);
        fetchVideos();
    }

    public void fetchVideos(){
        Message message = Message.obtain(customHandler);
        message.what = 0;
        message.sendToTarget();
    }

    public void resetRequestStatus(){
        request_status.postValue(NetworkState.IDLE);
    }

    public MutableLiveData<List<VideoResponseBody>> getVideosObserver() {
        return videosObserver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setVideos(List<VideoResponseBody> new_videos) {
        request_status.postValue(NetworkState.SUCCESS);
        videos.addAll(new_videos);
        videosObserver.postValue(videos);
    }

    public List<VideoResponseBody> getVideos() {
        return videos;
    }

    public MutableLiveData<NetworkState> getRequest_status() {
        return request_status;
    }

    private static class HomeFragmentThreadCustomHandler extends Handler{

        private HomeFragmentViewModel homeFragmentViewModel;
        private VideosDao videosDao;

        public HomeFragmentThreadCustomHandler(Looper looper, VideosDao videosDao, HomeFragmentViewModel homeFragmentViewModel) {
            super(looper);
            this.homeFragmentViewModel = homeFragmentViewModel;
            this.videosDao = videosDao;
        }

        public void fetchVideoList(){
            Call<List<VideoResponseBody>> call =  videosDao.fetchVideos("20", String.valueOf(homeFragmentViewModel.getVideos().size()));
            try {
                Response<List<VideoResponseBody>> response = call.execute();
                if(response.isSuccessful() && response.body() != null){
                    homeFragmentViewModel.setVideos(response.body());
                }else{
                    homeFragmentViewModel.getRequest_status().postValue(NetworkState.ERROR);
                    homeFragmentViewModel.setMessage("An error occurred while retrieving videos");
                }
            } catch (IOException e) {
                e.printStackTrace();
                homeFragmentViewModel.getRequest_status().postValue(NetworkState.ERROR);
                homeFragmentViewModel.setMessage("Couldn't connect to server. Please try again");
            }
        }
        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:
                    fetchVideoList();
                    break;
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        appHandlerThread.dispose();
    }
}
