package com.echoeyecodes.sinnerman.JobDispatchers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.LikeDao
import com.echoeyecodes.sinnerman.repository.LikeRepository
import com.echoeyecodes.sinnerman.repository.VideoRepository
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class LikeDispatch(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private val apiClient = ApiClient.getInstance(context).getClient(LikeDao::class.java)
    private val likeRepository: LikeRepository = LikeRepository(context)
    private val videoRepository: VideoRepository = VideoRepository(context)

    override fun doWork() : Result {
        var status = false

        val job = CoroutineScope(Dispatchers.IO).launch{
            status = withContext(Dispatchers.Unconfined) { sendLike() }
        }

        while(!job.isCompleted){
            try{
                CoroutineScope(Dispatchers.IO).launch {
                    delay(1500)
                }
            }catch (exception: InterruptedException){
                exception.printStackTrace()
            }
        }


        if(!status){
            return Result.retry()
        }
        return Result.success()
    }

    private suspend fun sendLike(): Boolean = withContext(Dispatchers.Unconfined) {
        val likes = likeRepository.getUnsentLikes()
        if(likes.isNotEmpty()){
            var state = false
            for ( likeModel in likes) {
                state = try {
                    val response = apiClient.sendLike(likeModel.type, likeModel)
                    videoRepository.updateVideoInDB(response)
                    likeRepository.deleteLike(likeModel)
                    true
                } catch (e : Exception) {
                    false
                }
            }
            return@withContext state
        }else{
            return@withContext true
        }
    }
}
