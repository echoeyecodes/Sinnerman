package com.example.myapplication.ViewModel.AuthViewModel;

import android.util.Log;
import com.example.myapplication.API.ApiUtils.RequestStatus;
import com.example.myapplication.Models.OtpModel;
import com.example.myapplication.Utils.AuthenticationManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class OtpViewModel extends AuthViewModel {

    public OtpViewModel() {
       super();

        initRequiredFields("otp");
    }

    @Override
    public void submitForm() {
        super.submitForm();
        if (isValid) {
            verify();
        }
    }

    private void verify() {
        OtpModel otpModel = new OtpModel(form_fields.get("otp"), getVerification_response());
        Call<ResponseBody> call = authDao.verify(otpModel);

        handler.post(new OtpViewModel.OtpApiExecutor(call, this));
    }

    private static class OtpApiExecutor implements Runnable {
        private Call<ResponseBody> call;
        private OtpViewModel otpViewModel;

        public OtpApiExecutor(Call<ResponseBody> call, OtpViewModel otpViewModel) {
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
