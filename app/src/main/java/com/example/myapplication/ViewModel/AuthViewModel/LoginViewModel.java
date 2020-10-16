package com.example.myapplication.ViewModel.AuthViewModel;

import android.util.Log;
import com.example.myapplication.API.ApiUtils.RequestStatus;
import com.example.myapplication.Models.UserModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class LoginViewModel extends AuthViewModel {

    private String verification_response;

    public LoginViewModel() {
       super();
       initRequiredFields("username", "password");
    }

    private void login() {
        UserModel userModel = new UserModel(form_fields.get("username"), form_fields.get("password"));
        Call<ResponseBody> call = authDao.login(userModel);

        handler.post(new LoginViewModel.LoginApiExecutor(call, this));
    }

    public String getVerification_response() {
        return verification_response;
    }

    public void setVerification_response(String verification_response) {
        this.verification_response = verification_response;
    }

    @Override
    public void submitForm() {
        super.submitForm();
        if (isValid) {
            login();
        }
    }

    private static class LoginApiExecutor implements Runnable {
        private Call<ResponseBody> call;
        private LoginViewModel loginViewModel;

        public LoginApiExecutor(Call<ResponseBody> call, LoginViewModel loginViewModel) {
            this.call = call;
            this.loginViewModel = loginViewModel;
        }

        @Override
        public void run() {
            try {
                requestStatusObserver.postValue(RequestStatus.LOADING);
                Response<ResponseBody> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    if (response.code() == 200) {
                        String token = response.body().string();
                        loginViewModel.setToken(token);
                        requestStatusObserver.postValue(RequestStatus.SUCCESS);
                    } else if (response.code() == 202) {
                        //extract email from response body here
                        loginViewModel.setVerification_response(response.body().string());
                        requestStatusObserver.postValue(RequestStatus.NOT_VERIFIED);
                    }

                } else {
                    if (response.errorBody() != null) {
                        loginViewModel.setMessage(response.errorBody().string());
                        requestStatusObserver.postValue(RequestStatus.ERROR);

                        Log.d("CARRR", response.errorBody().string());
                    }
                }
            } catch (IOException e) {
                loginViewModel.setMessage("Something went wrong with the request. Try again later");
                //Internal Server error
                requestStatusObserver.postValue(RequestStatus.ERROR);
            }
        }
    }
}
