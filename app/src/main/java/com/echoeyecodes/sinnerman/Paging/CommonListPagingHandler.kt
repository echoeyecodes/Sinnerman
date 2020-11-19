package com.echoeyecodes.sinnerman.Paging

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.echoeyecodes.sinnerman.Utils.Result
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

abstract class CommonListPagingHandler<T : Any>(application: Application) : AndroidViewModel(application){

    var hasMore = true
    var isRunning= false
    var networkStatus = MutableLiveData<Result<T>>()
    var state = ArrayList<T>()


    open fun initialize(){
        state.clear()
        state = ArrayList();
    }

    fun load(state: Result<T>){
        initialize()
        fetchMore(state)
    }

    fun fetchMore(state: Result<T>){
        networkStatus.postValue(state)
        if(hasMore && !isRunning){
            CoroutineScope(Dispatchers.IO).launch{
                async { fetchData() }.await()
            }
        }else{
            networkStatus.postValue(networkStatus.value)
        }
    }

    private fun resetState() {
        networkStatus.postValue(Result.Idle)
        isRunning = false
    }

    abstract suspend fun fetchList(): List<T>

    private suspend fun fetchData(){
        isRunning = true
        try{
            val result = fetchList()
            state.addAll(result)
            onDataReceived(state)
            if(result.isEmpty()){
                hasMore = false
            }
        }catch (error: IOException){
            networkStatus.postValue(Result.Error)
        }
        catch (error: HttpException){
            networkStatus.postValue(Result.Error)
        }finally {
            isRunning = false
        }
    }

    open suspend fun onDataReceived(result: List<T>){
        networkStatus.postValue(Result.Idle)
    }

    open fun refresh(){
        hasMore = true
        load(Result.Refreshing)
    }

    fun retry(){
        fetchMore(Result.Loading)
    }

    override fun onCleared() {
        super.onCleared()

        networkStatus.postValue(null)
        state.clear()
        isRunning=false
        hasMore = true

    }
}