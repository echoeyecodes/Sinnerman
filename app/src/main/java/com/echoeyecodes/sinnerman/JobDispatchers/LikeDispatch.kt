package com.echoeyecodes.sinnerman.JobDispatchers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.LikeDao
import com.echoeyecodes.sinnerman.repository.LikeRepository
import com.echoeyecodes.sinnerman.repository.VideoRepository
import kotlinx.coroutines.*

class LikeDispatch(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private val apiClient = ApiClient.getInstance(context).getClient(LikeDao::class.java)
    private val likeRepository: LikeRepository = LikeRepository(context)
    private val videoRepository: VideoRepository = VideoRepository(context)

    override fun doWork() : Result = runBlocking {
        var status = false

        val job = CoroutineScope(Dispatchers.IO).launch{
            status = withContext(Dispatchers.Unconfined) { sendLike() }
        }

        job.join()


        if(!status){
            return@runBlocking Result.retry()
        }
        return@runBlocking Result.success()
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
