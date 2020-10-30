package com.echoeyecodes.sinnerman.viewmodel;

import android.app.Application;
import android.util.Log
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.viewModelScope
import com.echoeyecodes.sinnerman.Models.LikeModel
import com.echoeyecodes.sinnerman.Models.ResolutionDimensions
import com.echoeyecodes.sinnerman.Models.VideoResponseBody;
import com.echoeyecodes.sinnerman.repository.LikeRepository
import com.echoeyecodes.sinnerman.repository.VideoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

import java.io.IOException;


class VideoActivityViewModel(application: Application) : AndroidViewModel(application) {
    private var fullscreen = false
    val isFullScreen = MutableLiveData<Boolean>(fullscreen)
    val request_status = MutableLiveData<NetworkState>(NetworkState.LOADING)
    val videoObserver = MutableLiveData<VideoResponseBody>()
    var resolutions = ArrayList<ResolutionDimensions>()
    var selectedItemPosition = 0;
    private val likeRepository = LikeRepository(application)
    private val videoRepository = VideoRepository(application)
    var didLike = MutableLiveData<Boolean>(false)

     fun getFullScreenValue() : Boolean{
        return fullscreen
    }

    fun toggleFullScreen(value: Boolean){
        isFullScreen.postValue(value)
        fullscreen = value
    }

    fun likeVideo(){
        val video = videoObserver.value
        val type = if(didLike.value!!){
            "1"
        }else{
            "0"
        }
        didLike.postValue(type == "0")
        video?.let {
            val like = LikeModel(it.video.id, type)
            likeRepository.addLikeToDB(like)
        }
    }

    fun fetchVideo(video_id: String){
        request_status.postValue(NetworkState.LOADING)
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                try{
                    val video = videoRepository.fetchVideo(video_id)
                    videoRepository.updateVideoInDB(video)
                    videoObserver.postValue(video)
                    didLike.postValue(video.video.has_liked)
                    Log.d("CARRR", video.video.has_liked.toString())
                    request_status.postValue(NetworkState.SUCCESS)
                }catch (exception: IOException){
                    request_status.postValue(NetworkState.ERROR)
                }
                catch (exception: HttpException){
                    request_status.postValue(NetworkState.ERROR)
                }
            }
        }
    }
}
