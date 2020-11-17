package com.echoeyecodes.sinnerman.Models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "upload_notifications")
data class UploadNotificationModel(@NonNull @PrimaryKey val id:String, val message:String,
                                   val timestamp:String,
                                   val profile_url: String,
                                   val thumbnail:String,
                                   var is_read:Boolean,
                                   var video_id:String): PagerModel()