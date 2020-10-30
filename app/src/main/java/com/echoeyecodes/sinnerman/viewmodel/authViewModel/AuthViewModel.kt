package com.echoeyecodes.sinnerman.viewmodel.authViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.echoeyecodes.sinnerman.API.ApiUtils.ApiClient
import com.echoeyecodes.sinnerman.API.DAO.AuthDao
import com.echoeyecodes.sinnerman.API.ApiUtils.RequestStatus
import com.echoeyecodes.sinnerman.Utils.FieldErrorStatus

import java.util.HashMap

open class AuthViewModel(application: Application) : AndroidViewModel(application) {
    val requestStatusObserver = MutableLiveData<RequestStatus>()
    val apiClient = ApiClient.getInstance(application.applicationContext)
    val authDao: AuthDao = apiClient.getClient(AuthDao::class.java)

    var isValid = true
    var token: String? = null

    var message = ""
    var verification_response = ""
    val form_fields = HashMap<String, String>()
    val errorObserver = MutableLiveData<FieldErrorStatus>()


    fun setRequestStatus(status: RequestStatus) {
        requestStatusObserver.value = status
    }


    fun validateFields() {
        for (entry in form_fields) {
            if (entry.value.isEmpty()) {
                val fieldErrorStatus = FieldErrorStatus(entry.key, entry.key.plus(" cannot be empty"), true)
                errorObserver.value = fieldErrorStatus
                isValid = false
            }
        }
    }


    fun initRequiredFields(vararg keys: String) {
        for (key in keys) {
            form_fields[key] = ""
        }
    }


    fun validateSingleField(key: String) {
        if (form_fields[key]!!.isEmpty()) {
            val fieldErrorStatus = FieldErrorStatus(key, key.plus(" cannot be empty"), true)
            errorObserver.setValue(fieldErrorStatus)
        } else {
            errorObserver.value = FieldErrorStatus(key, "", false)
            isValid = true
        }
    }


    open fun submitForm() {
        validateFields()
    }
}
