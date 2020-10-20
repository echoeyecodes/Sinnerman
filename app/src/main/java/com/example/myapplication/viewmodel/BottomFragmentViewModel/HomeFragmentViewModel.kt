package com.example.myapplication.viewmodel.BottomFragmentViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.Room.PersistenceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    var videosObserver = MutableLiveData<List<VideoResponseBody>>()
    private val videoDao : VideosDao = ApiClient.getInstance(application).getClient(VideosDao::class.java)
    private val videos = ArrayList<VideoResponseBody>()
    var hasReachedEnd = false
    private var isLoading = false
    private val QUERY_LIMIT = 5
    private val roomDao = PersistenceDatabase.getInstance(application).videoDao()


    init {
        CoroutineScope(Dispatchers.IO).launch {
            run {
                deleteCache()
                fetchVideosFromDB()
            }
        }
    }

    private suspend fun deleteCache(){
        CoroutineScope(CoroutineScope(Dispatchers.IO).launch {
            roomDao.deleteVideoAndUsers()
            videos.clear()
        })
    }

     fun fetchVideosFromDB(){
        if(!isLoading && !hasReachedEnd){
            isLoading = true
            CoroutineScope(Dispatchers.IO).launch {
                val result = roomDao.getVideos(videos.size, QUERY_LIMIT)
                when {
                    result.isEmpty() -> {
                        isLoading = false
                        fetchVideos(videos.size.toString())
                    }
                    result.isNotEmpty() -> {
                        videos.addAll(result)
                        Log.d("CARRR", "posted")
                        videosObserver.postValue(result)
                        isLoading = false
                    }
                }
            }
        }
    }

    fun refresh(){
        CoroutineScope(Dispatchers.IO).launch {
            deleteCache()
            hasReachedEnd = false
            videosObserver.postValue(ArrayList())
            roomDao.deleteVideoAndUsers()
            fetchVideosFromDB()
        }
    }

     private fun fetchVideos(offset:String) {
         if(!isLoading){
             isLoading = true
             CoroutineScope(Dispatchers.IO).launch {
                 val result = videoDao.fetchVideos(QUERY_LIMIT.toString(), offset)
                 if(result.isEmpty() || result.size < QUERY_LIMIT){
                     hasReachedEnd = true
                 }
                 result.forEach{
                     roomDao.insertVideoAndUsers(it)
                 }
                 fetchVideosFromDB()
                 isLoading = false
             }
         }

     }

}