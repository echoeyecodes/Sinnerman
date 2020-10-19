package com.example.myapplication.Models

import androidx.paging.PagingData

class ExploreResponseBody {

    var name: String? = null;
    var id: String = "";
    var videos: PagingData<VideoResponseBody>? = null
}