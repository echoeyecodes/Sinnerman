package com.echoeyecodes.sinnerman.viewmodel.BottomFragmentViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.liveData
import com.echoeyecodes.sinnerman.Models.UploadNotificationModel
import com.echoeyecodes.sinnerman.Paging.CommonListPagingHandler
import com.echoeyecodes.sinnerman.Utils.Result
import com.echoeyecodes.sinnerman.repository.NotificationRepository

class NotificationViewModel(application: Application) : CommonListPagingHandler<UploadNotificationModel>(application) {
    val notificationRepository = NotificationRepository(getApplication())

    init {
        load(Result.Loading)
    }

    override fun initialize() {
            notificationRepository.deleteNotifications()
            super.initialize()
    }

    fun getNotifications():LiveData<List<UploadNotificationModel>>{
        return Transformations.switchMap(notificationRepository.getNotificationsFromDB()) {
            liveData { emit(it) }
        }
    }


    override suspend fun fetchList(): List<UploadNotificationModel> {
        return notificationRepository.getNotifications(state.size.toString())
    }

    override suspend fun onDataReceived(result: List<UploadNotificationModel>) {
            super.onDataReceived(result)
            notificationRepository.addNotificationToDB(ArrayList(result))
    }
}