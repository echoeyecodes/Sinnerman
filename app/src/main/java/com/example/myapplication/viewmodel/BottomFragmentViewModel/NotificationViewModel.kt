package com.example.myapplication.viewmodel.BottomFragmentViewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Models.UploadNotificationModel
import com.example.myapplication.Paging.CommonListPagingHandler
import com.example.myapplication.repository.NotificationRepository
import com.example.myapplication.viewmodel.NetworkState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class NotificationViewModel(application: Application) : CommonListPagingHandler<UploadNotificationModel>(application) {
    val notificationRepository = NotificationRepository(getApplication())

    init {
        load(NetworkState.LOADING)
    }

    override fun initialize() {
            notificationRepository.deleteNotifications()
            super.initialize()
    }

    fun getNotifications():LiveData<List<UploadNotificationModel>>{
        return notificationRepository.getNotificationsFromDB()
    }


    override suspend fun fetchList(): List<UploadNotificationModel> {
        return notificationRepository.getNotifications(state.size.toString())
    }

    override suspend fun onDataReceived(result: List<UploadNotificationModel>) {
            notificationRepository.addNotificationToDB(ArrayList(result))
            super.onDataReceived(result)
    }
}