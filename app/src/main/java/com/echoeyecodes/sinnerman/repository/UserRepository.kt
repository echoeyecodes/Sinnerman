package com.echoeyecodes.sinnerman.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.UserDao
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserRepository(context: Context) {
    private val apiClient = ApiClient.getInstance(context).getClient(UserDao::class.java)
    val status = MutableLiveData<NetworkState>()

    fun uploadImage(body: MultipartBody.Part){
        status.postValue(NetworkState.LOADING)
        CoroutineScope(Dispatchers.IO).launch{
            try{
                apiClient.uploadPhoto(body)
                status.postValue(NetworkState.SUCCESS)
            }catch (exception : Exception){
                Log.d("CARRR", exception.message)
                status.postValue(NetworkState.ERROR)
            }

        }
    }

}