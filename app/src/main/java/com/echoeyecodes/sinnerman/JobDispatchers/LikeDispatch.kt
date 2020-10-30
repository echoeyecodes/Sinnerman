package com.echoeyecodes.sinnerman.JobDispatchers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.LikeDao
import com.echoeyecodes.sinnerman.repository.LikeRepository
import com.echoeyecodes.sinnerman.repository.VideoRepository
import kotlinx.coroutines.*
import retrofit2.HttpException

class LikeDispatch(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private val apiClient = ApiClient.getInstance(context).getClient(LikeDao::class.java)
    private val likeRepository: LikeRepository = LikeRepository(context)
    private val videoRepository: VideoRepository = VideoRepository(context)

    override fun doWork() : Result {
        var status = false

        val job = CoroutineScope(Dispatchers.IO).launch{
            status = withContext(Dispatchers.IO) { sendLike() }
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

    private suspend fun sendLike(): Boolean = withContext(Dispatchers.IO) {
        var status = false
        val likes = likeRepository.getUnsentLikes()
        if(likes.isNotEmpty()){
            for ( likeModel in likes) {
                status = try {
                    val response = apiClient.sendLike(likeModel.type, likeModel)
                    videoRepository.updateVideoInDB(response)
                    likeRepository.deleteLike(likeModel)
                    true
                } catch (e : HttpException) {
                    false
                }
            }
            status
        }else{
            true
        }
    }
}
