package com.echoeyecodes.sinnerman.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.VideosDao
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.Room.PersistenceDatabase
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

    suspend fun fetchVideo(id:String) : VideoResponseBody{
        return withContext(Dispatchers.IO){
            apiClient.fetchVideo(id)
        }
    }

    fun addVideosToDB(data:List<VideoResponseBody>){
        CoroutineScope(Dispatchers.IO).launch{
            videoDao.insertVideoAndUsers(data)
        }
    }

    fun getVideosFromDB() : LiveData<List<VideoResponseBody>>{
        return videoDao.getVideos()
    }

    fun getVideoFromDB(id: String) : VideoResponseBody{
        return videoDao.getVideo(id)
    }

}