package com.example.myapplication.Room.Dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.myapplication.Models.UploadNotificationModel
import com.example.myapplication.Models.UserModel
import com.example.myapplication.Models.VideoModel
import com.example.myapplication.Models.VideoResponseBody

@Dao
abstract class UploadNotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertNotification(notification: List<UploadNotificationModel>)

    @Query("SELECT * FROM upload_notifications")
    abstract fun getUploadNotifications(): LiveData<List<UploadNotificationModel>>

    @Query("DELETE FROM upload_notifications")
    abstract fun deleteUploadNotifications()

}