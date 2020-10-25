package com.example.myapplication.Paging

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.viewmodel.NetworkState
import kotlinx.coroutines.*
import java.io.IOException

abstract class CommonListPagingHandler<T>(application: Application) : AndroidViewModel(application){

    var hasMore = true
    val networkStatus = MutableLiveData<NetworkState>()
    var state = ArrayList<T>()


    init {
        CoroutineScope(Dispatchers.IO).launch {
            networkStatus.postValue(NetworkState.LOADING)
            load()
        }
    }

    abstract suspend fun initialize()

    private suspend fun load(){
        withContext(Dispatchers.IO){
            async { initialize() }.await()
            async { fetchMore(NetworkState.LOADING) }.await()
        }
    }

    fun fetchMore(state: NetworkState){
        networkStatus.postValue(state)
        if(hasMore){
            CoroutineScope(Dispatchers.IO).launch{
                async { fetchData() }.await()
            }
        }else{
            resetState()
        }
    }

    private fun resetState() {
        networkStatus.postValue(NetworkState.IDLE)
    }

    abstract suspend fun fetchList(): List<T>

    suspend fun fetchData(){
        try{
            val result = fetchList()
            state.addAll(result)
            onDataReceived(state)
            if(result.isEmpty() || result.size < 5){
                hasMore = false
            }
            networkStatus.postValue(NetworkState.SUCCESS)
        }catch (error: IOException){
            networkStatus.postValue(NetworkState.ERROR)
        }
    }

    abstract suspend fun onDataReceived(result: List<T>)

    fun refresh(){
        CoroutineScope(Dispatchers.IO).launch {
            networkStatus.postValue(NetworkState.REFRESHING)
            hasMore = true
            load()
        }
    }

    fun retry(){
        fetchMore(NetworkState.LOADING)
    }
}