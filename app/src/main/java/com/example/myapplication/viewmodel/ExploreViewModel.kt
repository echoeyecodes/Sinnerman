package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.*
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.ExploreResponseBody
import com.example.myapplication.Paging.ExplorePagingSource

class ExploreViewModel(application: Application) : AndroidViewModel(application){

    var videosObserver : LiveData<PagingData<ExploreResponseBody>>
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
            pagingSourceFactory = { ExplorePagingSource(videoDao = videoDao) }
    ).liveData.cachedIn(viewModelScope)
}