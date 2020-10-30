package com.example.myapplication.viewmodel;

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.myapplication.JobDispatchers.CommentDispatch
import com.example.myapplication.Models.CommentModel
import com.example.myapplication.Models.CommentResponseBody
import com.example.myapplication.Paging.CommonListPagingHandler
import com.example.myapplication.Room.CommentDatabase
import com.example.myapplication.Utils.AuthUserManager
import com.example.myapplication.repository.CommentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentActivityViewModel(application: Application) : CommonListPagingHandler<CommentResponseBody>(application) {
    val commentRepository = CommentRepository(getApplication())
    val commentDao = CommentDatabase.getInstance(getApplication()).commentDao()
    var video_id: String =""


    init {
        load(NetworkState.LOADING)
    }

     override fun initialize() {
         commentRepository.deleteComments()
         super.initialize()
     }

    fun sendComment(commentModel: CommentModel) {
        viewModelScope.launch(Dispatchers.IO){
            commentModel.video_id = video_id
            val currentUser = AuthUserManager.getInstance().getUser(getApplication())
            commentModel.user_id = currentUser.id

            val commentResponseBody = CommentResponseBody(currentUser, commentModel)
            commentRepository.addCommentToDB(commentResponseBody)
            initiateCommentWorkRequest()
        }
    }

     fun getComments() : LiveData<List<CommentResponseBody>>{
         val mediatorLiveData = MediatorLiveData<List<CommentResponseBody>>()
         mediatorLiveData.addSource(commentRepository.getCommentsFromDB(video_id)){ mediatorLiveData.postValue(it) }
         return mediatorLiveData
     }

    private fun initiateCommentWorkRequest(){
            val workRequest = OneTimeWorkRequest.Builder(CommentDispatch::class.java).build()
            val workManager = WorkManager.getInstance(getApplication())
            workManager.enqueue(workRequest)
    }

    override suspend fun fetchList(): List<CommentResponseBody> {
        return commentRepository.getComments(video_id, state.size.toString())
    }

     override suspend fun onDataReceived(result: List<CommentResponseBody>) {
             commentRepository.addCommentsToDB(ArrayList(result))
             super.onDataReceived(result)
     }

}