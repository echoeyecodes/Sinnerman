package com.echoeyecodes.sinnerman.JobDispatchers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.CommentDao
import com.echoeyecodes.sinnerman.repository.CommentRepository
import kotlinx.coroutines.*
import retrofit2.HttpException

class CommentDispatch(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private val apiClient = ApiClient.getInstance(context).getClient(CommentDao::class.java)
    private val commentRepository: CommentRepository = CommentRepository(context)

    override fun doWork() : Result {
        var status = false

            val job = CoroutineScope(Dispatchers.IO).launch{
                status = withContext(Dispatchers.IO) { sendComments() }
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

    private suspend fun sendComments(): Boolean = withContext(Dispatchers.IO) {
        var status = false
        val comments = commentRepository.getUnsentComments()
        if(comments.isNotEmpty()){
            for ( commentModel in comments) {
                status = try {
                    val response = apiClient.sendComment(commentModel)
                    commentRepository.updateCommentInDB(response)
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
