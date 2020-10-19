package com.example.myapplication.JobDispatchers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.CommentDao
import com.example.myapplication.Room.PersistenceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class CommentDispatch(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    private val commentDao : CommentDao
    private val persist_comment_dao : com.example.myapplication.Room.Dao.CommentDao
    private val apiClient : ApiClient
    private val persistenceDatabase : PersistenceDatabase

    init {
        persistenceDatabase = PersistenceDatabase.getInstance(context)
        persist_comment_dao = persistenceDatabase.commentDao()
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
        val comments = persist_comment_dao.unsentComments
        for ( commentModel in comments) {
        val call = commentDao.sendComment(commentModel)
            try {
            val response = call.execute()
            if (response.isSuccessful) {
                persist_comment_dao.insertCommentAndUser(response.body())
                status = true
            }
        } catch (e : IOException) {
            e.printStackTrace()
        }
    }
        return@withContext  status
    }

}
