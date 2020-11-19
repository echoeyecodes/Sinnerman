package com.echoeyecodes.sinnerman.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.UserDao
import com.echoeyecodes.sinnerman.Models.UserModel
import com.echoeyecodes.sinnerman.Utils.AuthUserManager
import com.echoeyecodes.sinnerman.Utils.PreferenceManager
import com.echoeyecodes.sinnerman.util.AppHandlerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import java.io.IOException

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    var userModel: UserModel? = null
    val apiClient = ApiClient.getInstance(application)
    val userDao = apiClient.getClient(UserDao::class.java)
    val preferenceManager: PreferenceManager = PreferenceManager(application)
    val isLoaded = MutableLiveData<Boolean>()

    private fun setCurrentUser(user: UserModel?) {
        userModel = user
        val authUserManager = AuthUserManager.getInstance()
        authUserManager.saveUser(this, userModel)
    }

     fun updateCurrentUser(){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val userModel = userDao.getCurrentUser()
                setCurrentUser(userModel)
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}