package com.example.myapplication.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.viewModelScope
import com.example.myapplication.API.ApiUtils.ApiClient;
import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Models.VideoResponseBody;
import com.example.myapplication.Room.Dao.VideoDao;
import com.example.myapplication.Room.PersistenceDatabase;
import com.example.myapplication.util.AppHandlerThread;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

class VideoActivityViewModel(application: Application) : AndroidViewModel(application) {
    private var fullscreen = false
    val isFullScreen = MutableLiveData<Boolean>(fullscreen)
    val request_status = MutableLiveData<NetworkState>(NetworkState.LOADING);
    val videoObserver = MutableLiveData<VideoResponseBody>()
    private val video: VideoResponseBody? = null
    private val videosDao: VideosDao
    private val persistDao : VideoDao
    private var message:String = ""

    init {
        videosDao = ApiClient.getInstance(application.applicationContext).getClient(VideosDao::class.java)
        persistDao = PersistenceDatabase.getInstance(application.applicationContext).videoDao()
    }

     fun getFullScreenValue() : Boolean{
        return fullscreen
    }

    fun toggleFullScreen(value: Boolean){
        isFullScreen.postValue(value)
        fullscreen = value
    }

    fun fetchVideo(video_id: String){
        request_status.postValue(NetworkState.LOADING)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try{
                    val video = videosDao.fetchVideo(video_id)
                    persistDao.updateVideoAndUser(video)
                    videoObserver.postValue(video)
                    request_status.postValue(NetworkState.SUCCESS)
                }catch (exception: IOException){
                    request_status.postValue(NetworkState.ERROR)
                }

            }
        }
    }
}
