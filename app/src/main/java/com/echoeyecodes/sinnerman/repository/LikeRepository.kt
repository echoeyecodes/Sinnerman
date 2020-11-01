package com.echoeyecodes.sinnerman.repository

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.echoeyecodes.sinnerman.JobDispatchers.LikeDispatch
import com.echoeyecodes.sinnerman.Models.LikeModel
import com.echoeyecodes.sinnerman.Room.PersistenceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class LikeRepository(private val context: Context){
    val likeDao = PersistenceDatabase.getInstance(context.applicationContext).likeDao()

    fun addLikeToDB(likeModel: LikeModel){
        CoroutineScope(Dispatchers.IO).launch {
            likeDao.insertLike(likeModel)
            initiateLikeWorkRequest()
        }
    }

    suspend fun deleteLike(likeModel: LikeModel){
        withContext(Dispatchers.Unconfined){
            likeDao.deleteLike(likeModel)
        }
    }

    fun getUnsentLikes():List<LikeModel>{
        return likeDao.getAllLikes()
    }

    private fun initiateLikeWorkRequest(){
        val workRequest = OneTimeWorkRequest.Builder(LikeDispatch::class.java).setInitialDelay(1500, TimeUnit.MILLISECONDS).addTag("like_work_request").build()
        val workManager = WorkManager.getInstance(context.applicationContext)
        workManager.enqueueUniqueWork("like_work_request", ExistingWorkPolicy.REPLACE, workRequest)
    }
}