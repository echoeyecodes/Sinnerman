package com.example.myapplication.viewmodel.authViewModel

import android.app.Application
import android.util.Log
import com.example.myapplication.API.ApiUtils.RequestStatus
import com.example.myapplication.Models.OtpModel
import com.example.myapplication.Utils.AuthenticationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

import java.io.IOException

class OtpViewModel(application: Application) : AuthViewModel(application) {

    init {
        initRequiredFields("otp")
    }

    override fun submitForm() {
        super.submitForm()
        if (isValid) {
            CoroutineScope(Dispatchers.IO).launch { verify() }
        }
    }

    private suspend fun verify() = withContext(Dispatchers.IO){
        val otpModel = OtpModel(form_fields["otp"], verification_response)
        val call = authDao.verify(otpModel)

        try {
            requestStatusObserver.postValue(RequestStatus.LOADING)
            val response = call.execute()

            if (response.isSuccessful && response.body() != null) {
                val result = AuthenticationManager().tokenExtractor(response.body()!!.string())
                token = result
                requestStatusObserver.postValue(RequestStatus.SUCCESS)
            } else {
                if (response.errorBody() != null) {
                    message =  response.errorBody()!!.string()
                    requestStatusObserver.postValue(RequestStatus.ERROR)

                    Log.d("CARRR", response.errorBody()!!.string())
                }
            }
        } catch (e : IOException) {
            message = "Something went wrong with the request. Try again later"
            //Internal Server error
            requestStatusObserver.postValue(RequestStatus.ERROR)
        }
    }
}
