package com.example.myapplication.ViewModel.AuthViewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.API.DAO.ApiClient;
import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Utils.VideoThreadHandler;
import com.example.myapplication.ViewModel.NetworkState;

public class MainViewModel extends AndroidViewModel {
    public MutableLiveData<NetworkState> network_state = new MutableLiveData<>(NetworkState.LOADING);
    public ApiClient apiClient;
    public static VideoThreadHandler handlerThread;
    private String message;

    public VideosDao videosDao;

    public MainViewModel(@NonNull Application application) {
        super(application);

        apiClient = new ApiClient(application.getApplicationContext());

        if(handlerThread == null){
            handlerThread = new VideoThreadHandler("MAIN_ACTIVITY_THREAD", this, videosDao);
            handlerThread.start();
        }
        videosDao =  apiClient.getClient(VideosDao.class);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        handlerThread.quit();
        handlerThread = null;
    }

}
