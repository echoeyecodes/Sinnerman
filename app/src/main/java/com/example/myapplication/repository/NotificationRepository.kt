package com.example.myapplication.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.UploadNotificationDao
import com.example.myapplication.Models.UploadNotificationModel
import com.example.myapplication.Room.PersistenceDatabase
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
        return apiClient.fetchUploadNotifications("5", offset)
    }

    fun deleteNotifications(){
        CoroutineScope(Dispatchers.IO).launch {
            notificationDao.deleteUploadNotifications()
        }
    }

}