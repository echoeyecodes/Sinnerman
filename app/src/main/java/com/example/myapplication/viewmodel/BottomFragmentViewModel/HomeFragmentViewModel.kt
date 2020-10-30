package com.example.myapplication.viewmodel.BottomFragmentViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.Paging.CommonListPagingHandler
import com.example.myapplication.Room.Dao.VideoDao
import com.example.myapplication.Room.PersistenceDatabase
import com.example.myapplication.repository.VideoRepository
import com.example.myapplication.viewmodel.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

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