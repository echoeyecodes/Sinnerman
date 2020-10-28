package com.example.myapplication.JobDispatchers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.CommentDao
import com.example.myapplication.Room.CommentDatabase
import com.example.myapplication.Room.PersistenceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CommentDispatch(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private val commentDao : CommentDao
    private val persist_comment_dao : com.example.myapplication.Room.Dao.CommentDao
    private val apiClient : ApiClient
    private val commentDatabase : CommentDatabase

    init {
        commentDatabase = CommentDatabase.getInstance(context)!!
        persist_comment_dao = commentDatabase.commentDao()!!
        apiClient =  ApiClient.getInstance(context)
        commentDao = apiClient.getClient(CommentDao::class.java)
    }

    override fun doWork() : Result {
        var status = false

        CoroutineScope(Dispatchers.IO).launch {
            status = withContext(Dispatchers.Default) { sendComments() }
        }

        if(!status){
            return Result.retry()
        }
        return Result.success()
    }

    private suspend fun sendComments(): Boolean = withContext(Dispatchers.IO) {
        var status = false
        val comments = persist_comment_dao.getUnsentComments()
        for ( commentModel in comments) {
            status = try {
                val response = commentDao.sendComment(commentModel)
                persist_comment_dao.insertCommentAndUser(response)
                true
            } catch (e : HttpException) {
                false
            }
    }
        return@withContext  status
    }

}
