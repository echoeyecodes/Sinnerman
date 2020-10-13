package com.example.myapplication.ViewModel.AuthViewModel;

import android.os.Handler;
import android.os.HandlerThread;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.API.DAO.ApiClient;
import com.example.myapplication.API.DAO.AuthDao;
import com.example.myapplication.API.DAO.RequestStatus;
import com.example.myapplication.Utils.FieldErrorStatus;

import java.util.HashMap;
import java.util.Map;

public abstract class AuthViewModel extends ViewModel {
    public HandlerThread handlerThread;
    public Handler handler;
    public ApiClient apiClient = new ApiClient();;
    public AuthDao authDao;
    public boolean isValid = true;
    private String token;
    private String message ="";
    public final Map<String, String> form_fields = new HashMap<>();
    public final MutableLiveData<FieldErrorStatus> errorObserver = new MutableLiveData<>();
    public static final MutableLiveData<RequestStatus> requestStatusObserver = new MutableLiveData<>();

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

    public void setRequestStatus(RequestStatus requestStatus){
        requestStatusObserver.setValue(requestStatus);
    }

    public void setForm_field(String key, String value) {
        form_fields.put(key, value);
    }

    public abstract void submitForm();
}
