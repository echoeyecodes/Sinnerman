package com.echoeyecodes.sinnerman.viewmodel.authViewModel

import android.app.Application
import android.util.Log
import com.echoeyecodes.sinnerman.API.ApiUtils.RequestStatus
import com.echoeyecodes.sinnerman.Models.UserModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class SignUpViewModel(application: Application) : AuthViewModel(application) {

    init{
        initRequiredFields("username", "password", "firstname", "lastname", "email")
    }

    private suspend fun createAccount() : Unit = withContext(Dispatchers.IO){
        val userModel = UserModel()
        userModel.email =form_fields["email"]!!
        userModel.password= form_fields["password"]!!
        userModel.username= form_fields["username"]!!
        userModel.fullname= form_fields["firstname"].plus(" ").plus(form_fields["lastname"])
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
                    Log.d("CARRR", response.errorBody()!!.string())
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
