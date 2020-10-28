package com.example.myapplication.Paging

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.viewmodel.NetworkState
import kotlinx.coroutines.*
import java.io.IOException

abstract class CommonListPagingHandler<T>(application: Application) : AndroidViewModel(application){

    var hasMore = true
    var isRunning= false
    val networkStatus = MutableLiveData<NetworkState>(NetworkState.IDLE)
    var state = ArrayList<T>()


    open fun initialize(){
        state.clear()
        state = java.util.ArrayList();
        networkStatus.postValue(NetworkState.LOADING)
    }

    fun load(){
        initialize()
        fetchMore(NetworkState.LOADING)
    }

    fun fetchMore(state: NetworkState){
        networkStatus.postValue(state)
        if(hasMore && !isRunning){
            CoroutineScope(Dispatchers.IO).launch{
                async { fetchData() }.await()
            }
        }else{
            resetState()
        }
    }

    private fun resetState() {
        networkStatus.postValue(NetworkState.IDLE)
        isRunning = false
    }

    abstract suspend fun fetchList(): List<T>

    suspend fun fetchData(){
        isRunning = true
        try{
            val result = fetchList()
            state.addAll(result)
            onDataReceived(state)
            if(result.isEmpty() || result.size < 5){
                hasMore = false
            }
        }catch (error: IOException){
            networkStatus.postValue(NetworkState.ERROR)
        }
        isRunning = false
    }

    open suspend fun onDataReceived(result: List<T>){
        networkStatus.postValue(NetworkState.SUCCESS)
    }

    fun refresh(){
        viewModelScope.launch {
            networkStatus.postValue(NetworkState.REFRESHING)
            hasMore = true
            load()
        }
    }

    fun retry(){
        fetchMore(NetworkState.LOADING)
    }
}