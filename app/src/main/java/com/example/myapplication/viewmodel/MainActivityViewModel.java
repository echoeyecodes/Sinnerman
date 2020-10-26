package com.example.myapplication.viewmodel;

import android.app.Application;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.API.ApiUtils.ApiClient;
import com.example.myapplication.API.DAO.UserDao;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.Utils.AuthUserManager;
import com.example.myapplication.util.AppHandlerThread;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

public class MainActivityViewModel extends AndroidViewModel {

    private final MainActivityCustomHandler customHandler;
    private UserModel userModel;
    private final MutableLiveData<Boolean> isLoaded = new MutableLiveData<>();

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        AppHandlerThread appHandlerThread = AppHandlerThread.getInstance();
        ApiClient apiClient = ApiClient.getInstance(application);
        UserDao userDao = apiClient.getClient(UserDao.class);
        HandlerThread handlerThread = appHandlerThread.getHandlerThread();
        customHandler = new MainActivityCustomHandler(handlerThread.getLooper(), userDao, this);
    }


    public void updateCurrentUser(){
        Message message = Message.obtain(customHandler);
        message.sendToTarget();
    }

    private void setCurrentUser(UserModel user){
        this.userModel = user;
        AuthUserManager authUserManager = AuthUserManager.getInstance();
        authUserManager.saveUser(this, this.userModel);
    }

    public MutableLiveData<Boolean> getIsLoaded() {
        return isLoaded;
    }

    public UserModel getUserModel() {
        return userModel;
    }


    private static class MainActivityCustomHandler extends Handler {

        private final UserDao userDao;
        private final MainActivityViewModel mainActivityViewModel;

        public MainActivityCustomHandler(Looper looper, UserDao userDao, MainActivityViewModel mainActivityViewModel) {
            super(looper);

            this.userDao = userDao;
            this.mainActivityViewModel = mainActivityViewModel;

        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            Call<UserModel> call = userDao.getCurrentUser();
            try {
                Response<UserModel> response = call.execute();
                if(response.isSuccessful() && response.body() != null){
                    mainActivityViewModel.setCurrentUser(response.body());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
