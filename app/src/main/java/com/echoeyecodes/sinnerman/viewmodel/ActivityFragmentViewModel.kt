package com.echoeyecodes.sinnerman.viewmodel

import android.app.Application
import com.echoeyecodes.sinnerman.Models.VideoResponseBody

class ActivityFragmentViewModel(application: Application) : VideoListViewModel(application) {

    var context:String = ""
    override suspend fun fetchList(): List<VideoResponseBody> {
        return videoRepository.getVideoActivity(context, state.size.toString())
    }

}