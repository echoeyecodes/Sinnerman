package com.example.myapplication.viewmodel.BottomFragmentViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.Paging.VideoPagingSource
import com.example.myapplication.viewmodel.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    var request_status : LiveData<NetworkState> = MutableLiveData<NetworkState>()
     var videosObserver : LiveData<PagingData<VideoResponseBody>>? = null
     private val videoDao : VideosDao = ApiClient.getInstance(application).getClient(VideosDao::class.java)


    init {
        videosObserver = fetchVideos()
    }

     private fun fetchVideos() = Pager(
             config = PagingConfig(
                     pageSize = 5,
                     maxSize = 100,
                     enablePlaceholders = false
             ),
             pagingSourceFactory = {VideoPagingSource(videoDao = videoDao, initialLoadSize = 0)}
     ).liveData.cachedIn(viewModelScope)

    override fun onCleared() {
        super.onCleared()
        videosObserver = null
    }
}