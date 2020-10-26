package com.example.myapplication.viewmodel.authViewModel

import android.app.Application
import com.example.myapplication.API.ApiUtils.RequestStatus
import com.example.myapplication.Models.UserModel
import kotlinx.coroutines.*

import java.io.IOException

class SignUpViewModel(application: Application) : AuthViewModel(application) {

    init{
        initRequiredFields("username", "password", "firstname", "lastname", "email")
    }

    private suspend fun createAccount() : Unit = withContext(Dispatchers.IO){
        val userModel = UserModel(form_fields["firstname"].plus(" ").plus(form_fields["lastname"]), form_fields["username"]!!, form_fields["email"]!!, form_fields["password"]!!)
        val call = authDao.createUser(userModel)

        try {
            requestStatusObserver.postValue(RequestStatus.LOADING)
            val response = call.execute()

            if(response.isSuccessful && response.body() != null){
                val status = response.code()
                if(status == 200){
                    verification_response = response.body()!!.string()
                    requestStatusObserver.postValue(RequestStatus.SUCCESS)
                }else if(status == 202){
                    requestStatusObserver.postValue(RequestStatus.EXISTS)
                }

            }else{
                if(response.errorBody() != null){
                    message = response.errorBody()!!.string()
                }
                requestStatusObserver.postValue(RequestStatus.ERROR)
            }
        } catch (e : IOException) {
            message = "Something went wrong with the request. Try again later"
            requestStatusObserver.postValue(RequestStatus.ERROR)
        }
    }

    override fun submitForm() {
        super.submitForm()
        if(isValid){
            CoroutineScope(Dispatchers.IO).launch{createAccount()}
        }
    }
}
