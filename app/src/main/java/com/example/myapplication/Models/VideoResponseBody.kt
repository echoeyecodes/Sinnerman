package com.example.myapplication.Models;

import androidx.room.Embedded

data class VideoResponseBody(@Embedded val video: VideoModel, @Embedded val user:UserModel)