package com.echoeyecodes.sinnerman.viewmodel;

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.echoeyecodes.sinnerman.Models.LikeModel
import com.echoeyecodes.sinnerman.Models.ResolutionDimensions
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.repository.LikeRepository
import com.echoeyecodes.sinnerman.repository.VideoRepository
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.IOException


class VideoActivityViewModel(application: Application) : AndroidViewModel(application) {
    private var fullscreen = false
    val isFullScreen = MutableLiveData<Boolean>(fullscreen)
    val request_status = MutableLiveData<NetworkState>(NetworkState.LOADING)
    val videoObserver = MutableLiveData<VideoResponseBody>()
    var resolutions = ArrayList<ResolutionDimensions>()
    var defaultTrackSelector:DefaultTrackSelector = DefaultTrackSelector(application)
    var selectedItemPosition = 0;
    private val likeRepository = LikeRepository(application)
    private val leastRecentlyUsedCacheEvictor = LeastRecentlyUsedCacheEvictor(Long.MAX_VALUE)
    val simpleCache: SimpleCache = SimpleCache(File(application.cacheDir, "media"), leastRecentlyUsedCacheEvictor, ExoDatabaseProvider(application))
    private val videoRepository = VideoRepository(application)
    var didLike = MutableLiveData<Boolean>(false)

    init {
        defaultTrackSelector.setParameters(defaultTrackSelector.buildUponParameters().setMaxVideoSizeSd())
    }

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

    override fun onCleared() {
        simpleCache.release()
        super.onCleared()
    }
}
