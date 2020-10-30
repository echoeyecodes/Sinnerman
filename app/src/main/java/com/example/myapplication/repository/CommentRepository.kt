package com.example.myapplication.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.CommentDao
import com.example.myapplication.Models.CommentModel
import com.example.myapplication.Models.CommentResponseBody
import com.example.myapplication.Room.CommentDatabase
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