package com.example.myapplication.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "videos")
 data class VideoModel (@PrimaryKey @NonNull
                        val id : String,
                        val title : String,
                        val description: String,
                        val createdAt: String,
                        val updatedAt: String,
                        val thumbnail: String, var views: Int = 0,
                        val video_url: String,
                        @ColumnInfo(name = "video_user_id")
val user_id : String)

