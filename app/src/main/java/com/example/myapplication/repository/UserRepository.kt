package com.example.myapplication.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.UserDao
import com.example.myapplication.viewmodel.NetworkState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.lang.Exception

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