package com.example.myapplication.viewmodel.BottomFragmentViewModel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.*
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.Paging.VideoPagingMediator
import com.example.myapplication.Paging.VideoPagingSource
import com.example.myapplication.Room.Dao.VideoDao
import com.example.myapplication.Room.PersistenceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val videoDao: VideosDao = ApiClient.getInstance(application).getClient(VideosDao::class.java)
    private val QUERY_LIMIT = 5
    private val roomDao:VideoDao = PersistenceDatabase.getInstance(application).videoDao()
    var videos : LiveData<PagingData<VideoResponseBody>>


    init {
        videos = fetchVideos()
    }

    fun fetchVideos() : LiveData<PagingData<VideoResponseBody>> {
        return Pager(
                config = PagingConfig(
                        pageSize = QUERY_LIMIT,
                        enablePlaceholders = false,
                        maxSize = 100
                ),
                pagingSourceFactory = { VideoPagingSource(videoDao) },
                remoteMediator = VideoPagingMediator(videoDao, getApplication(), 0)
        ).liveData.cachedIn(viewModelScope)
    }

    fun insertUpdateToVideoList(video: VideoResponseBody){
        CoroutineScope(Dispatchers.IO).launch { roomDao.insertVideoAndUsers(video) }
    }
}