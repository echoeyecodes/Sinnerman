package com.echoeyecodes.sinnerman.repository

import android.content.Context
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.echoeyecodes.sinnerman.JobDispatchers.LikeDispatch
import com.echoeyecodes.sinnerman.Models.LikeModel
import com.echoeyecodes.sinnerman.Room.PersistenceDatabase
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