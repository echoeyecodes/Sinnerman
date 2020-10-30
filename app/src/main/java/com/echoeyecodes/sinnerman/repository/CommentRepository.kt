package com.echoeyecodes.sinnerman.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.CommentDao
import com.echoeyecodes.sinnerman.Models.CommentModel
import com.echoeyecodes.sinnerman.Models.CommentResponseBody
import com.echoeyecodes.sinnerman.Room.CommentDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentRepository(context: Context){
    val apiClient = ApiClient.getInstance(context.applicationContext).getClient(CommentDao::class.java)
    val commentDao = CommentDatabase.getInstance(context.applicationContext).commentDao()

    fun addCommentToDB(commentResponseBody: CommentResponseBody){
        commentDao.insertCommentAndUser(commentResponseBody)
    }

    fun updateCommentInDB(commentResponseBody: CommentResponseBody){
        commentDao.updateCommentAndUser(commentResponseBody)
    }

    fun addCommentsToDB(commentResponseBody: List<CommentResponseBody>){
        commentDao.insertCommentAndUsers(commentResponseBody)
    }

    fun getCommentsFromDB(id: String):LiveData<List<CommentResponseBody>>{
        return liveData { emitSource(commentDao.getComments(id)) }
    }

    fun deleteComments(){
        CoroutineScope(Dispatchers.IO).launch {
            commentDao.deleteAllCommentData()
        }
    }

    suspend fun getComments(id:String, offset:String) : List<CommentResponseBody>{
        return apiClient.getComments(id, "10", offset)
    }


    suspend fun getUnsentComments(): List<CommentModel>{
        return commentDao.getUnsentComments()
    }
}