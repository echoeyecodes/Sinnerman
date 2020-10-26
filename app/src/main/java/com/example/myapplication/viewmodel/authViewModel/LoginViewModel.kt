package com.example.myapplication.viewmodel.authViewModel

import android.app.Application
import android.util.Log
import com.example.myapplication.API.ApiUtils.RequestStatus
import com.example.myapplication.Models.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class LoginViewModel(application: Application) : AuthViewModel(application) {

    init{
       initRequiredFields("username", "password")
    }

    private suspend fun login() : Unit = withContext(IO){
        val userModel = UserModel(form_fields["username"]!!, form_fields["password"]!!)
        val call = authDao.login(userModel)

        try {
            requestStatusObserver.postValue(RequestStatus.LOADING)
            val response = call.execute()

            if (response.isSuccessful && response.body() != null) {
                if (response.code() == 200) {
                    val result = response.body()!!.string()
                    token = result
                    requestStatusObserver.postValue(RequestStatus.SUCCESS)
                } else if (response.code() == 202) {
                    //extract email from response body here
                    verification_response = response.body()!!.string()
                    requestStatusObserver.postValue(RequestStatus.NOT_VERIFIED)
                }

            } else {
                if (response.errorBody() != null) {
                    message = response.errorBody()!!.string()
                    requestStatusObserver.postValue(RequestStatus.ERROR)
                    Log.d("CARRR", response.errorBody()!!.string())
                }
            }
        } catch (e: IOException) {
            message = "Something went wrong with the request. Try again later"
            //Internal Server error
            requestStatusObserver.postValue(RequestStatus.ERROR)
        }
    }

    override fun submitForm() {
        super.submitForm()
        if (isValid) {
            CoroutineScope(IO).launch{ login() }
        }
    }
}
