package com.example.myapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.Room.PersistenceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoRepository(context: Context) {
    val apiClient = ApiClient.getInstance(context.applicationContext).getClient(VideosDao::class.java)
    val videoDao = PersistenceDatabase.getInstance(context.applicationContext).videoDao()

    suspend fun getVideos(offset:String):List<VideoResponseBody>{
        return apiClient.fetchVideos("5", offset)
    }

     fun deleteVideosFromDB(){
         CoroutineScope(Dispatchers.IO).launch {
             videoDao.deleteVideoAndUsers()
         }

    }

    suspend fun updateVideoInDB(videoResponseBody: VideoResponseBody){
        videoDao.updateVideoAndUser(videoResponseBody)
    }

    fun addVideosToDB(data:List<VideoResponseBody>){
        CoroutineScope(Dispatchers.IO).launch{
            videoDao.insertVideoAndUsers(data)
        }
    }

    fun getVideosFromDB() : LiveData<List<VideoResponseBody>>{
        return videoDao.getVideos()
    }

}