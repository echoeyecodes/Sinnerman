package com.example.myapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.CommentDao
import com.example.myapplication.Models.CommentResponseBody
import com.example.myapplication.Room.CommentDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentRepository(context: Context){
    val apiClient = ApiClient.getInstance(context.applicationContext).getClient(CommentDao::class.java)
    val commentDao = CommentDatabase.getInstance(context.applicationContext).commentDao()

    suspend fun addCommentToDB(commentResponseBody: CommentResponseBody){
        commentDao.insertCommentAndUser(commentResponseBody)
    }

    fun addCommentsToDB(commentResponseBody: List<CommentResponseBody>){
        commentDao.insertCommentAndUsers(commentResponseBody)
    }

    fun getCommentsFromDB():LiveData<List<CommentResponseBody>>{
        return commentDao.getComments()
    }

    fun deleteComments(){
        CoroutineScope(Dispatchers.IO).launch {
            commentDao.deleteAllComment()
        }
    }

    suspend fun getComments(id:String, offset:String) : List<CommentResponseBody>{
        return apiClient.getComments(id, "10", offset)
    }

}