package com.example.myapplication.JobDispatchers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.CommentDao
import com.example.myapplication.API.DAO.LikeDao
import com.example.myapplication.Room.CommentDatabase
import com.example.myapplication.Room.PersistenceDatabase
import com.example.myapplication.repository.CommentRepository
import com.example.myapplication.repository.LikeRepository
import com.example.myapplication.repository.VideoRepository
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
