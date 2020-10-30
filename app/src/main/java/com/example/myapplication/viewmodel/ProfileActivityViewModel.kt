package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.myapplication.repository.UserRepository
import okhttp3.MultipartBody

class ProfileActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository = UserRepository(application)

    fun uploadPhoto(body: MultipartBody.Part){
        userRepository.uploadImage(body)
    }

    fun getStatus():LiveData<NetworkState>{
        return userRepository.status
    }

}