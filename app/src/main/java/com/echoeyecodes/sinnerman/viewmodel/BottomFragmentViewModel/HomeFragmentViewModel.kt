package com.echoeyecodes.sinnerman.viewmodel.BottomFragmentViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.Paging.CommonListPagingHandler
import com.echoeyecodes.sinnerman.repository.VideoRepository
import com.echoeyecodes.sinnerman.viewmodel.NetworkState

class HomeFragmentViewModel(application: Application) : CommonListPagingHandler<VideoResponseBody>(application) {
    val videoRepository = VideoRepository(getApplication())

    init {
        load(NetworkState.LOADING)
    }

    override fun initialize() {
            videoRepository.deleteVideosFromDB()
            super.initialize()
    }

    fun getVideos():LiveData<List<VideoResponseBody>>{
        return videoRepository.getVideosFromDB()
    }

    override suspend fun fetchList(): List<VideoResponseBody> {
        return videoRepository.getVideos(state.size.toString())
    }

    override suspend fun onDataReceived(result: List<VideoResponseBody>) {
       videoRepository.addVideosToDB(ArrayList(result))
            super.onDataReceived(result)
    }
}