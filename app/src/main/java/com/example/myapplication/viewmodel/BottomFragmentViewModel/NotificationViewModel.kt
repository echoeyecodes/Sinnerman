package com.example.myapplication.viewmodel.BottomFragmentViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.Models.UploadNotificationModel
import com.example.myapplication.Paging.CommonListPagingHandler
import com.example.myapplication.Room.Dao.UploadNotificationDao
import com.example.myapplication.Room.PersistenceDatabase
import kotlinx.coroutines.*
import kotlin.collections.ArrayList
import kotlin.coroutines.coroutineContext

class NotificationViewModel(application: Application) : CommonListPagingHandler<UploadNotificationModel>(application) {
    private val uploadNotificationDao: com.example.myapplication.API.DAO.UploadNotificationDao = ApiClient.getInstance(application.applicationContext).getClient(com.example.myapplication.API.DAO.UploadNotificationDao::class.java)
    val roomDao: UploadNotificationDao = PersistenceDatabase.getInstance(application.applicationContext).uploadNotificationDao()



    override suspend fun initialize() {
        viewModelScope.launch(coroutineContext) {
            roomDao.deleteUploadNotifications()
            super.initialize()
        }
    }


    override suspend fun fetchList(): List<UploadNotificationModel> {
        return uploadNotificationDao.fetchUploadNotifications("5", state.size.toString())
    }

    override suspend fun onDataReceived(result: List<UploadNotificationModel>) {
        withContext(coroutineContext) {
            async { roomDao.insertNotification(ArrayList(result)) }.await()
            super.onDataReceived(result)
        }
    }
}