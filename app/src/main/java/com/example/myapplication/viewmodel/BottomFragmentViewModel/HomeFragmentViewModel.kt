package com.example.myapplication.viewmodel.BottomFragmentViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.Paging.CommonListPagingHandler
import com.example.myapplication.Room.Dao.VideoDao
import com.example.myapplication.Room.PersistenceDatabase
import com.example.myapplication.viewmodel.NetworkState
import kotlinx.coroutines.*
import java.io.IOException
import java.util.ArrayList
import kotlin.coroutines.coroutineContext

class HomeFragmentViewModel(application: Application) : CommonListPagingHandler<VideoResponseBody>(application) {
    private val videoDao: VideosDao = ApiClient.getInstance(application.applicationContext).getClient(VideosDao::class.java)
    val roomDao: VideoDao = PersistenceDatabase.getInstance(application.applicationContext).videoDao()


    fun insertUpdateToVideoList(item: VideoResponseBody) {
        // Testing views update on video watch
        val (video, user) = item
        val views = video.views + 1
        val updatedVideo = video.copy(views = views)
        val updatedData = VideoResponseBody(updatedVideo, user)
        CoroutineScope(Dispatchers.IO).launch { roomDao.updateVideoAndUser(updatedData) }
    }

    override suspend fun initialize() {
        viewModelScope.launch(coroutineContext) {
            roomDao.deleteVideoAndUsers()
            super.initialize()
        }
    }


    override suspend fun fetchList(): List<VideoResponseBody> {
        return videoDao.fetchVideos("5", state.size.toString())
    }

    override suspend fun onDataReceived(result: List<VideoResponseBody>) {
        withContext(coroutineContext){
            async { roomDao.insertVideoAndUsers(result) }.await()
            super.onDataReceived(result)
        }
    }

}