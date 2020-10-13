package com.example.myapplication.ViewModel.AuthViewModel;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.example.myapplication.API.DAO.RequestStatus;
import com.example.myapplication.API.DAO.AuthDao;
import com.example.myapplication.Models.UserModel;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

public class SignUpViewModel extends AuthViewModel {

    public SignUpViewModel() {
        handlerThread = new HandlerThread("SIGN_UP_THREAD");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        authDao = apiClient.getClient(AuthDao.class);
        initRequiredFields("username", "password", "firstname", "lastname", "email");
    }

    private void createAccount(){
        UserModel userModel = new UserModel(form_fields.get("firstname").concat(" ").concat(form_fields.get("lastname")), form_fields.get("username"), form_fields.get("email"), form_fields.get("password"));
        Call<String> call = authDao.createUser(userModel);

        handler.post(new ApiExecutor(call, this));
    }

    @Override
    public void submitForm() {
        super.submitForm();
        if(isValid){
            createAccount();
        }
    }

    private static class ApiExecutor implements Runnable{
        private Call<String> call;
        private SignUpViewModel signUpViewModel;

        public ApiExecutor(Call<String> call, SignUpViewModel signUpViewModel){
            this.call = call;
            this.signUpViewModel = signUpViewModel;
        }

        @Override
        public void run() {
            try {
                requestStatusObserver.postValue(RequestStatus.LOADING);
                Response<String> response = call.execute();

                if(response.isSuccessful() && response.body() != null){
                    if(response.body().equals("ok")){
                        requestStatusObserver.postValue(RequestStatus.SUCCESS);
                    }else{
                        requestStatusObserver.postValue(RequestStatus.EXISTS);
                    }

                }else{
                    requestStatusObserver.postValue(RequestStatus.ERROR);

                    if(response.errorBody() != null){
                        //Internal Server error
                        Log.d("CARRR", response.errorBody().string());
                    }
                }
            } catch (IOException e) {
                signUpViewModel.setMessage("Something went wrong with the request. Try again later");
                //Internal Server error
                requestStatusObserver.postValue(RequestStatus.ERROR);
            }
        }
    }
}
