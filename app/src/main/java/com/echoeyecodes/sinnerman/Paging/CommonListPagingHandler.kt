package com.echoeyecodes.sinnerman.Paging

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

abstract class CommonListPagingHandler<T>(application: Application) : AndroidViewModel(application){

    var hasMore = true
    var isRunning= false
    var networkStatus = MutableLiveData<NetworkState>()
    var state = ArrayList<T>()


    open fun initialize(){
        state.clear()
        state = ArrayList();
    }

    fun load(state: NetworkState){
        initialize()
        fetchMore(state)
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
        networkStatus.postValue(NetworkState.SUCCESS)
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
        catch (error: HttpException){
            Log.d("CARRR", error.message())
            networkStatus.postValue(NetworkState.ERROR)
        }
        isRunning = false
    }

    open suspend fun onDataReceived(result: List<T>){
        networkStatus.postValue(NetworkState.SUCCESS)
    }

    fun refresh(){
        Log.d("CARRR", "Resfrsjig")
        hasMore = true
        load(NetworkState.REFRESHING)
    }

    fun retry(){
        fetchMore(NetworkState.LOADING)
    }

    override fun onCleared() {
        super.onCleared()

        networkStatus.postValue(null)
        state.clear()
        isRunning=false
        hasMore = true

    }
}