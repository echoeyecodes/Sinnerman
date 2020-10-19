package com.example.myapplication.viewmodel.authViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.AuthDao
import com.example.myapplication.API.ApiUtils.RequestStatus
import com.example.myapplication.Utils.FieldErrorStatus

import java.util.HashMap

open class AuthViewModel : ViewModel() {
    val requestStatusObserver = MutableLiveData<RequestStatus>()
    val apiClient = ApiClient.getInstance()
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
