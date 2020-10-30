package com.echoeyecodes.sinnerman.Room.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.echoeyecodes.sinnerman.Models.UploadNotificationModel

@Dao
abstract class UploadNotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertNotification(notification: List<UploadNotificationModel>)

    @Query("SELECT * FROM upload_notifications")
    abstract fun getUploadNotifications(): LiveData<List<UploadNotificationModel>>

    @Query("DELETE FROM upload_notifications")
    abstract fun deleteUploadNotifications()

}