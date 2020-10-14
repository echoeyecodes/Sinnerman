package com.example.myapplication.ViewModel;

import android.app.Application;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.API.DAO.ApiClient;
import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Models.VideoResponseBody;
import com.example.myapplication.Utils.VideoThreadHandler;
import com.example.myapplication.ViewModel.AuthViewModel.MainViewModel;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class HomeFragmentViewModel extends MainViewModel {
    private MutableLiveData<List<VideoResponseBody>> videos;

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);

        if(videos == null){
            videos = new MutableLiveData<>();
            fetchVideos();
        }
    }

    public void fetchVideos(){
        network_state.setValue(NetworkState.LOADING);
        Message message = Message.obtain(handlerThread.getHandler());
        message.what = 0;
        message.obj = "20";
        message.sendToTarget();
    }

    public MutableLiveData<NetworkState> getNetwork_state() {
        return network_state;
    }

    public void setNetwork_state(MutableLiveData<NetworkState> network_state) {
        this.network_state = network_state;
    }

    public MutableLiveData<List<VideoResponseBody>> getVideos() {
        return videos;
    }

}
