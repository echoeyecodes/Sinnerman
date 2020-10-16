package com.example.myapplication.ViewModel.BottomFragmentViewModel;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import com.example.myapplication.API.ApiUtils.ApiClient;
import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Models.VideoResponseBody;
import com.example.myapplication.Paging.DataSource.VideoDataSource;
import com.example.myapplication.Paging.DataSourceFactory.VideoDataSourceFactory;
import com.example.myapplication.ViewModel.NetworkState;
import com.example.myapplication.util.AppHandlerThread;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragmentViewModel extends AndroidViewModel {
    private LiveData<NetworkState> request_status = new MutableLiveData<>(NetworkState.LOADING);
    private LiveData videosObserver;
    private List<VideoResponseBody> videos = new ArrayList<>();
    private String message;



    public HomeFragmentViewModel(Application application){
        super(application);

        fetchVideos();
    }

    public void fetchVideos(){
        VideoDataSourceFactory videoDataSourceFactory = new VideoDataSourceFactory(getApplication());
        setRequest_status(Transformations.switchMap(videoDataSourceFactory.getVideoDataSourceMutableLiveData(), VideoDataSource::getRequest_status));

        PagedList.Config.Builder pagedListBuilder = new PagedList.Config.Builder();
        PagedList.Config config1 = pagedListBuilder.setEnablePlaceholders(false).setInitialLoadSizeHint(5).setPageSize(5).build();

        videosObserver = new LivePagedListBuilder(videoDataSourceFactory, config1).build();
    }

    public void resetRequestStatus(){

    }

    public void setRequest_status(LiveData<NetworkState> request_status) {
        this.request_status = request_status;
    }

    public LiveData<PagedList<VideoResponseBody>> getVideosObserver() {
        return videosObserver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setVideos(List<VideoResponseBody> new_videos) {
        videos.addAll(new_videos);
    }

    public List<VideoResponseBody> getVideos() {
        return videos;
    }

    public LiveData<NetworkState> getRequest_status() {
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

        @Override
        public void handleMessage(@NonNull @NotNull Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 0:

                    break;
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

    }
}
