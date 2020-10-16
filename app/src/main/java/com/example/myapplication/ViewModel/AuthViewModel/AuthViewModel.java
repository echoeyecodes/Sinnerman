package com.example.myapplication.ViewModel.AuthViewModel;

import android.os.Handler;
import android.os.HandlerThread;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.API.ApiUtils.ApiClient;
import com.example.myapplication.API.DAO.AuthDao;
import com.example.myapplication.API.ApiUtils.RequestStatus;
import com.example.myapplication.Utils.FieldErrorStatus;

import java.util.HashMap;
import java.util.Map;

public class AuthViewModel extends ViewModel {
    public static HandlerThread handlerThread;
    public static Handler handler;
    public static ApiClient apiClient = new ApiClient();
    public static AuthDao authDao;
    public boolean isValid = true;
    private String token;
    private String message ="";
    private String verification_response = "";
    public final Map<String, String> form_fields = new HashMap<>();
    public final MutableLiveData<FieldErrorStatus> errorObserver = new MutableLiveData<>();
    public static final MutableLiveData<RequestStatus> requestStatusObserver = new MutableLiveData<>();

    public AuthViewModel(){
        if(handlerThread == null){
            handlerThread = new HandlerThread("AUTH_THREAD");
            handlerThread.start();
        }
        handler = new Handler(handlerThread.getLooper());
        authDao = apiClient.getClient(AuthDao.class);
    }

    public void validateFields() {
        for (Map.Entry<String, String> entry : form_fields.entrySet()) {
            if (entry.getValue().isEmpty()) {
                FieldErrorStatus fieldErrorStatus = new FieldErrorStatus(entry.getKey(), entry.getKey().concat(" cannot be empty"),true );
                errorObserver.setValue(fieldErrorStatus);
                isValid = false;
            }
        }
    }

    public void initRequiredFields(String... keys) {
        for (String key : keys) {
            form_fields.put(key, "");
        }
    }

    public void validateSingleField(String key) {
        if(form_fields.get(key).isEmpty()){
            FieldErrorStatus fieldErrorStatus = new FieldErrorStatus(key, key.concat(" cannot be empty"), true);
            errorObserver.setValue(fieldErrorStatus);
        }else{
            errorObserver.setValue(new FieldErrorStatus(key, "", false));
            isValid = true;
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MutableLiveData<FieldErrorStatus> getErrorObserver() {
        return errorObserver;
    }

    public Map<String, String> getForm_fields() {
        return form_fields;
    }

    public MutableLiveData<RequestStatus> getRequestStatusObserver() {
        return requestStatusObserver;
    }

    public String getVerification_response() {
        return verification_response;
    }

    public void setVerification_response(String verification_response) {
        this.verification_response = verification_response;
    }

    public void setRequestStatus(RequestStatus requestStatus){
        requestStatusObserver.setValue(requestStatus);
    }

    public void setForm_field(String key, String value) {
        form_fields.put(key, value);
    }

    public void submitForm(){
        validateFields();
    }

    public void dispose(){
        handlerThread.quit();
        handlerThread = null;
    }
}
