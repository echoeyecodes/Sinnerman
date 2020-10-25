package com.example.myapplication.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.ExploreResponseBody
import com.example.myapplication.Models.VideoResponseBody
import kotlinx.coroutines.*
import java.io.IOException
import kotlin.coroutines.coroutineContext

class ExploreViewModel(application: Application) : AndroidViewModel(application) {

    private val videoDao: VideosDao = ApiClient.getInstance(application).getClient(VideosDao::class.java)
    var hasMore = true
    var isRefreshing = MutableLiveData<Boolean>(false)
    var data = ArrayList<ExploreResponseBody>()
    var categories: MutableLiveData<List<ExploreResponseBody>> = MutableLiveData()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            load()
        }
    }

    private suspend fun load() {
        withContext(Dispatchers.IO) {
            async { data.clear() }.await()
            async { fetchMore() }.await()
        }
    }

    fun fetchMore() {
        if (hasMore) {
            CoroutineScope(Dispatchers.IO).launch {
                val offset = data.size.toString()
                async { fetchData(offset) }.await()
            }
        }
    }

    suspend fun fetchData(offset: String) {
        try {
            val result = videoDao.fetchExplore("5", offset)
            data.addAll(result)
            val newData = ArrayList(data)
            if (result.isEmpty()) {
                hasMore = false
            }
            categories.postValue(newData)
        } catch (exception: IOException) {
            exception.printStackTrace()
        } finally {
            isRefreshing.postValue(false)
        }
    }


    fun refresh() {
        CoroutineScope(Dispatchers.IO).launch {
            isRefreshing.postValue(true)
            hasMore = true
            load()
        }
    }
}