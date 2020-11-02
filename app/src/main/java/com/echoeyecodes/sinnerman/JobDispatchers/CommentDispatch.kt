package com.echoeyecodes.sinnerman.JobDispatchers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.CommentDao
import com.echoeyecodes.sinnerman.repository.CommentRepository
import com.echoeyecodes.sinnerman.util.AppHandlerThread
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException
import kotlin.coroutines.coroutineContext

class CommentDispatch(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private val apiClient = ApiClient.getInstance(context).getClient(CommentDao::class.java)
    private val commentRepository: CommentRepository = CommentRepository(context)

    override fun doWork() : Result {
        var status = false

            val job = CoroutineScope(Dispatchers.IO).launch{
                status = withContext(Dispatchers.Unconfined) { sendComments() }
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

    private suspend fun sendComments(): Boolean = withContext(coroutineContext) {
        var status = false
        val comments = commentRepository.getUnsentComments()
        if(comments.isNotEmpty()){
            for ( commentModel in comments) {
                status = try {
                    val response = apiClient.sendComment(commentModel)
                    commentRepository.addCommentToDB(response)
                    true
                }  catch (e : Exception) {
                    false
                }
            }
            status
        }else{
            true
        }
    }
}
