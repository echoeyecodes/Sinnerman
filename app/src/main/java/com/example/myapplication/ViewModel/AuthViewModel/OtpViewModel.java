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

public class OtpViewModel extends AuthViewModel {

    private String email;

    public OtpViewModel() {
        handlerThread = new HandlerThread("OTP_THREAD");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        authDao = apiClient.getClient(AuthDao.class);
        initRequiredFields("otp");
    }

    @Override
    public void submitForm() {
        if (isValid) {
            verify();
        }
    }

    public void setEmail(String email) {
        this.email = email;
    }


    private void verify() {
        OtpModel otpModel = new OtpModel(form_fields.get("otp"), "femiobajuluwa@gmail.com");
        Call<ResponseBody> call = authDao.verify(otpModel);

        handler.post(new OtpViewModel.ApiExecutor(call, this));
    }

    private static class ApiExecutor implements Runnable {
        private Call<ResponseBody> call;
        private OtpViewModel otpViewModel;

        public ApiExecutor(Call<ResponseBody> call, OtpViewModel otpViewModel) {
            this.call = call;
            this.otpViewModel = otpViewModel;
        }

        @Override
        public void run() {
            try {
                requestStatusObserver.postValue(RequestStatus.LOADING);
                Response<ResponseBody> response = call.execute();

                if (response.isSuccessful() && response.body() != null) {
                    String token = new AuthenticationManager().tokenExtractor(response.body().string());
                    otpViewModel.setToken(token);
                    requestStatusObserver.postValue(RequestStatus.SUCCESS);
                } else {
                    if (response.errorBody() != null) {
                        otpViewModel.setMessage(response.errorBody().string());
                        requestStatusObserver.postValue(RequestStatus.ERROR);

                        Log.d("CARRR", response.errorBody().string());
                    }
                }
            } catch (IOException e) {
                otpViewModel.setMessage("Something went wrong with the request. Try again later");
                //Internal Server error
                requestStatusObserver.postValue(RequestStatus.ERROR);
            }
        }
    }
}
