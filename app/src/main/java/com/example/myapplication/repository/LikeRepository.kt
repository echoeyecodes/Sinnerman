package com.example.myapplication.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.CommentDao
import com.example.myapplication.JobDispatchers.CommentDispatch
import com.example.myapplication.JobDispatchers.LikeDispatch
import com.example.myapplication.Models.CommentModel
import com.example.myapplication.Models.CommentResponseBody
import com.example.myapplication.Models.LikeModel
import com.example.myapplication.Room.CommentDatabase
import com.example.myapplication.Room.Dao.VideoDao
import com.example.myapplication.Room.PersistenceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LikeRepository(private val context: Context){
    val likeDao = PersistenceDatabase.getInstance(context.applicationContext).likeDao()

    fun addLikeToDB(likeModel: LikeModel){
        CoroutineScope(Dispatchers.IO).launch {
            likeDao.insertLike(likeModel)
            initiateLikeWorkRequest()
        }
    }

    fun deleteLike(likeModel: LikeModel){
        likeDao.deleteLike(likeModel)
    }

    fun getUnsentLikes():List<LikeModel>{
        return likeDao.getAllLikes()
    }

    private fun initiateLikeWorkRequest(){
        val workRequest = OneTimeWorkRequest.Builder(LikeDispatch::class.java).build()
        val workManager = WorkManager.getInstance(context)
        workManager.enqueue(workRequest)
    }
}