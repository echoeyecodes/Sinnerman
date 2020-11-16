package com.echoeyecodes.sinnerman.viewmodel.BottomFragmentViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.Paging.CommonListPagingHandler
import com.echoeyecodes.sinnerman.Utils.Result
import com.echoeyecodes.sinnerman.repository.VideoRepository

class HomeFragmentViewModel(application: Application) : CommonListPagingHandler<VideoResponseBody>(application) {
    val videoRepository = VideoRepository(getApplication())

    init {
        load(Result.Loading)
    }

    override fun initialize() {
            videoRepository.deleteVideosFromDB()
            super.initialize()
    }

    fun getVideos():LiveData<List<VideoResponseBody>>{
        return Transformations.switchMap(videoRepository.getVideosFromDB()) {
            liveData { emit(it) }
        }
    }

    override suspend fun fetchList(): List<VideoResponseBody> {
        return videoRepository.getVideos(state.size.toString())
    }

    override suspend fun onDataReceived(result: List<VideoResponseBody>) {
        super.onDataReceived(result)
        videoRepository.addVideosToDB(ArrayList(result))
    }
}