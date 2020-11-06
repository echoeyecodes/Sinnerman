package com.echoeyecodes.sinnerman.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.VideosDao
import com.echoeyecodes.sinnerman.Models.ExploreResponseBody
import com.echoeyecodes.sinnerman.Paging.CommonListPagingHandler
import com.echoeyecodes.sinnerman.Utils.Result
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class ExploreViewModel(application: Application) : CommonListPagingHandler<ExploreResponseBody>(application) {

    private val videoDao: VideosDao = ApiClient.getInstance(application.applicationContext).getClient(VideosDao::class.java)
    var categories: MutableLiveData<List<ExploreResponseBody>> = MutableLiveData()

    init {
        load(Result.Loading)
    }

    override fun initialize() {
            categories.postValue(ArrayList())
            super.initialize()
    }

    override suspend fun fetchList(): List<ExploreResponseBody> {
        return withContext(coroutineContext){
            videoDao.fetchExplore("5", state.size.toString())
        }
    }

    override suspend fun onDataReceived(result: List<ExploreResponseBody>) {
            super.onDataReceived(result)
            categories.postValue(ArrayList(result))
        }
    }