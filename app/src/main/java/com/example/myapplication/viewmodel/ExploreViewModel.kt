package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.ExploreResponseBody
import com.example.myapplication.Paging.CommonListPagingHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class ExploreViewModel(application: Application) : CommonListPagingHandler<ExploreResponseBody>(application) {

    private val videoDao: VideosDao = ApiClient.getInstance(application.applicationContext).getClient(VideosDao::class.java)
    var categories: MutableLiveData<List<ExploreResponseBody>> = MutableLiveData()

    init {
        load(NetworkState.LOADING)
    }

    override fun initialize() {
            categories.postValue(null)
            super.initialize()
    }

    override suspend fun fetchList(): List<ExploreResponseBody> {
        return withContext(coroutineContext){
            videoDao.fetchExplore("5", state.size.toString())
        }
    }

    override suspend fun onDataReceived(result: List<ExploreResponseBody>) {
            categories.postValue(ArrayList(result))
            super.onDataReceived(result)
        }
    }