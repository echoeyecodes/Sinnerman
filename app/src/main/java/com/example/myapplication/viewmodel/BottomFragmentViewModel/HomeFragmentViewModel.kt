package com.example.myapplication.viewmodel.BottomFragmentViewModel

import android.app.Application
import androidx.lifecycle.*
import androidx.paging.*
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.Room.Dao.VideoDao
import com.example.myapplication.Room.PersistenceDatabase
import com.example.myapplication.viewmodel.NetworkState
import kotlinx.coroutines.*
import java.io.IOException

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val videoDao: VideosDao = ApiClient.getInstance(application).getClient(VideosDao::class.java)
    var hasMore = true
    val networkStatus = MutableLiveData<NetworkState>()
    val roomDao: VideoDao = PersistenceDatabase.getInstance(application).videoDao()


    init {
        CoroutineScope(Dispatchers.IO).launch {
            networkStatus.postValue(NetworkState.LOADING)
            load()
        }
    }

    private suspend fun load(){
        withContext(Dispatchers.IO){
            async { roomDao.deleteVideoAndUsers() }.await()
            async { fetchMore() }.await()
        }
    }


    fun fetchMore(){
        if(hasMore){
            CoroutineScope(Dispatchers.IO).launch{
                val offset = roomDao.getVideosList().size.toString()
                async { fetchData(offset) }.await()
            }
        }
    }

    suspend fun fetchData(offset : String){
        try{
            val result = videoDao.fetchVideos("5", offset)
            roomDao.insertVideoAndUsers(result)
            if(result.isEmpty() || result.size < 5){
                hasMore = false
            }
            networkStatus.postValue(NetworkState.SUCCESS)
        }catch (error: IOException){
            networkStatus.postValue(NetworkState.ERROR)
        }
    }

    fun refresh(){
        CoroutineScope(Dispatchers.IO).launch {
            networkStatus.postValue(NetworkState.REFRESHING)
            hasMore = true
            load()
        }
    }

    fun insertUpdateToVideoList(item: VideoResponseBody) {
        // Testing views update on video watch
        val (video, user) = item
        val views = video.views + 1
        val updatedVideo = video.copy(views = views)
        val updatedData = VideoResponseBody(updatedVideo, user)
        CoroutineScope(Dispatchers.IO).launch { roomDao.updateVideoAndUser(updatedData) }
    }

}