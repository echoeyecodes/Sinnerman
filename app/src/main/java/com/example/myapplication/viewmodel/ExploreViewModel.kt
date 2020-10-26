package com.example.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.ExploreResponseBody
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.Paging.CommonListPagingHandler
import kotlinx.coroutines.*
import java.io.IOException
import kotlin.coroutines.coroutineContext

class ExploreViewModel(application: Application) : CommonListPagingHandler<ExploreResponseBody>(application) {

    private val videoDao: VideosDao = ApiClient.getInstance(application.applicationContext).getClient(VideosDao::class.java)
    var categories: MutableLiveData<List<ExploreResponseBody>> = MutableLiveData()


    override suspend fun initialize() {
        categories.postValue(null)
        super.initialize()
    }

    override suspend fun fetchList(): List<ExploreResponseBody> {
        return videoDao.fetchExplore("5", state.size.toString())
    }

    override suspend fun onDataReceived(result: List<ExploreResponseBody>) {
        withContext(coroutineContext){
         async { categories.postValue(ArrayList(result)) }.await()
            super.onDataReceived(result)
        }
    }

}