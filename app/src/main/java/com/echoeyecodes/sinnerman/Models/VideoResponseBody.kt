package com.echoeyecodes.sinnerman.Models;

import androidx.room.Embedded
import androidx.room.Ignore

data class VideoResponseBody(@Embedded val video: VideoModel, @Embedded val user:UserModel) : PagerModel()