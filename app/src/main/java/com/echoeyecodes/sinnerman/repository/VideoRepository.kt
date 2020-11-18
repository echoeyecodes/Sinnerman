package com.echoeyecodes.sinnerman.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.VideosDao
import com.echoeyecodes.sinnerman.Models.ExploreResponseBody
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.Room.PersistenceDatabase
import com.echoeyecodes.sinnerman.Utils.PreferenceManager
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first

class VideoRepository(context: Context) {
    val apiClient = ApiClient.getInstance(context.applicationContext).getClient(VideosDao::class.java)
    val videoDao = PersistenceDatabase.getInstance(context.applicationContext).videoDao()
    val preferenceManager = PreferenceManager(context)



    suspend fun getVideos(offset:String):List<VideoResponseBody>{
        return apiClient.fetchVideos("10", offset)
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

    suspend fun fetchExplore(offset:String) : List<ExploreResponseBody>{
        return withContext(Dispatchers.IO){
            apiClient.fetchExplore( "10", offset)
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

    suspend fun getVideoByTag(id: String, offset: String) : List<VideoResponseBody>{
        return apiClient.fetchExploreItem(id, "10", offset)
    }

    suspend fun getVideoActivity(id: String, offset: String) : List<VideoResponseBody>{
        return apiClient.fetchVideoActivity(id, "10", offset)
    }

}