package com.example.myapplication.ViewModel.AuthViewModel;

import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.example.myapplication.API.DAO.RequestStatus;
import com.example.myapplication.API.DAO.AuthDao;
import com.example.myapplication.Models.UserModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Objects;

public class SignUpViewModel extends AuthViewModel {

    public SignUpViewModel() {
        super();
        initRequiredFields("username", "password", "firstname", "lastname", "email");
    }

    private void createAccount(){
        UserModel userModel = new UserModel(form_fields.get("firstname").concat(" ").concat(form_fields.get("lastname")), form_fields.get("username"), form_fields.get("email"), form_fields.get("password"), "");
        Call<ResponseBody> call = authDao.createUser(userModel);

        handler.post(new SignUpApiExecutor(call, this));
    }

    @Override
    public void submitForm() {
        super.submitForm();
        if(isValid){
            createAccount();
        }
    }

    private static class SignUpApiExecutor implements Runnable{
        private final Call<ResponseBody> call;
        private final SignUpViewModel signUpViewModel;

        public SignUpApiExecutor(Call<ResponseBody> call, SignUpViewModel signUpViewModel){
            this.call = call;
            this.signUpViewModel = signUpViewModel;
        }

        @Override
        public void run() {
            try {
                requestStatusObserver.postValue(RequestStatus.LOADING);
                Response<ResponseBody> response = call.execute();

                if(response.isSuccessful() && response.body() != null){
                    int status = response.code();
                    if(status == 200){
                        signUpViewModel.setVerification_response(response.body().string());
                        requestStatusObserver.postValue(RequestStatus.SUCCESS);
                    }else if(status == 202){
                        requestStatusObserver.postValue(RequestStatus.EXISTS);
                    }

                }else{
                    if(response.errorBody() != null){
                        signUpViewModel.setMessage(response.errorBody().string());
                    }
                    requestStatusObserver.postValue(RequestStatus.ERROR);
                }
            } catch (IOException e) {
                signUpViewModel.setMessage("Something went wrong with the request. Try again later");
                //Internal Server error
                requestStatusObserver.postValue(RequestStatus.ERROR);
            }
        }
    }
}
