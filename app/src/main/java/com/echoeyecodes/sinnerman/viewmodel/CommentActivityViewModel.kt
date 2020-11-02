package com.echoeyecodes.sinnerman.viewmodel;

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.echoeyecodes.sinnerman.JobDispatchers.CommentDispatch
import com.echoeyecodes.sinnerman.Models.CommentModel
import com.echoeyecodes.sinnerman.Models.CommentResponseBody
import com.echoeyecodes.sinnerman.Paging.CommonListPagingHandler
import com.echoeyecodes.sinnerman.Room.CommentDatabase
import com.echoeyecodes.sinnerman.Utils.AuthUserManager
import com.echoeyecodes.sinnerman.repository.CommentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CommentActivityViewModel(application: Application) : CommonListPagingHandler<CommentResponseBody>(application) {
    private val commentRepository = CommentRepository(getApplication())
    var video_id: String =""


    init {
        viewModelScope.launch {
            load(NetworkState.LOADING)
        }
    }

     override fun initialize() {
         commentRepository.deleteComments()
         super.initialize()
     }

    fun sendComment(commentModel: CommentModel) {
            commentModel.video_id = video_id
            val currentUser = AuthUserManager.getInstance().getUser(getApplication())
            commentModel.user_id = currentUser.id

            val commentResponseBody = CommentResponseBody(currentUser, commentModel)
            commentRepository.addCommentToDB(commentResponseBody)
    }

     fun getComments() : LiveData<List<CommentResponseBody>>{
         val mediatorLiveData = MediatorLiveData<List<CommentResponseBody>>()
         mediatorLiveData.addSource(commentRepository.getCommentsFromDB(video_id)){
             mediatorLiveData.postValue(it)
         }
         return mediatorLiveData
     }

    override suspend fun fetchList(): List<CommentResponseBody> {
        return commentRepository.getComments(video_id, state.size.toString())
    }

     override suspend fun onDataReceived(result: List<CommentResponseBody>) {
                super.onDataReceived(result)
             commentRepository.addCommentsToDB(ArrayList(result))
     }

}
