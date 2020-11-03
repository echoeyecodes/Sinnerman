package com.echoeyecodes.sinnerman.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "likes")
data class LikeModel(@PrimaryKey val video_id:String, val type:String)