package com.echoeyecodes.sinnerman.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.Paging.CommonListPagingHandler
import com.echoeyecodes.sinnerman.Utils.Result
import com.echoeyecodes.sinnerman.repository.VideoRepository

class VideoListViewModel(application: Application) : CommonListPagingHandler<VideoResponseBody>(application)  {

    val videoRepository = VideoRepository(getApplication())
    var videos = MutableLiveData<List<VideoResponseBody>>()
    var tag_id:String = ""

    init {
        load(Result.Loading)
    }

    override fun initialize() {
        videos.postValue(ArrayList())
        super.initialize()
    }

    fun getVideos(): LiveData<List<VideoResponseBody>> {
        return videoRepository.getVideosFromDB()
    }

    override suspend fun fetchList(): List<VideoResponseBody> {
        return videoRepository.getVideoByTag(tag_id, state.size.toString())
    }

    override suspend fun onDataReceived(result: List<VideoResponseBody>) {
        super.onDataReceived(result)
        videos.postValue(result)
    }
}