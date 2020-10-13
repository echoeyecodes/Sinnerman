package com.example.myapplication.ViewModel.AuthViewModel;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.example.myapplication.API.DAO.AuthDao;
import com.example.myapplication.API.DAO.RequestStatus;
import com.example.myapplication.Models.OtpModel;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.Utils.AuthenticationManager;
import com.google.gson.Gson;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

public class LoginViewModel extends AuthViewModel {

    public LoginViewModel() {
        handlerThread = new HandlerThread("LOGIN_THREAD");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        authDao = apiClient.getClient(AuthDao.class);
        initRequiredFields("username", "password");
    }

    private void login() {
        UserModel userModel = new UserModel(form_fields.get("username"), form_fields.get("password"));
        Call<ResponseBody> call = authDao.login(userModel);

        handler.post(new LoginViewModel.ApiExecutor(call, this));
    }

    @Override
    public void submitForm() {
        super.submitForm();
        if (isValid) {
            login();
        }
    }

    private static class ApiExecutor implements Runnable {
        private Call<ResponseBody> call;
        private LoginViewModel loginViewModel;

        public ApiExecutor(Call<ResponseBody> call, LoginViewModel loginViewModel) {
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
                        String token = new AuthenticationManager().tokenExtractor(response.body().string());
                        loginViewModel.setToken(token);
                        requestStatusObserver.postValue(RequestStatus.SUCCESS);
                    } else if (response.code() == 202) {
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
