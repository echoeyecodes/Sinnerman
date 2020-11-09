package com.echoeyecodes.sinnerman.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.UploadNotificationDao
import com.echoeyecodes.sinnerman.Models.UploadNotificationModel
import com.echoeyecodes.sinnerman.Room.PersistenceDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationRepository(context: Context) {
    val apiClient = ApiClient.getInstance(context.applicationContext).getClient(UploadNotificationDao::class.java)
    val notificationDao = PersistenceDatabase.getInstance(context.applicationContext).uploadNotificationDao()

    fun addNotificationToDB(data: List<UploadNotificationModel>){
        notificationDao.insertNotification(data)
    }

    fun getNotificationsFromDB() : LiveData<List<UploadNotificationModel>>{
        return notificationDao.getUploadNotifications()
    }

    suspend fun getNotifications(offset: String) : List<UploadNotificationModel>{
        return apiClient.fetchUploadNotifications("10", offset)
    }

    fun deleteNotifications(){
        CoroutineScope(Dispatchers.IO).launch {
            notificationDao.deleteUploadNotifications()
        }
    }

}