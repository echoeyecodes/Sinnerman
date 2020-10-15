package com.example.myapplication.ViewModel;

import android.app.Application;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.API.DAO.ApiClient;
import com.example.myapplication.API.DAO.UserDao;
import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.Utils.AuthUser;
import com.example.myapplication.util.AppHandlerThread;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private UserDao userDao;
    private ApiClient apiClient;
    private AppHandlerThread appHandlerThread;
    private HandlerThread handlerThread;
    private MainActivityCustomHandler customHandler;
    private UserModel userModel;
    private MutableLiveData<Boolean> isLoaded = new MutableLiveData<>();
    private Context context;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();

        appHandlerThread = AppHandlerThread.getInstance();
        apiClient = new ApiClient(application.getApplicationContext());
        userDao = apiClient.getClient(UserDao.class);
        handlerThread = appHandlerThread.getHandlerThread();
        customHandler = new MainActivityCustomHandler(handlerThread.getLooper(), userDao, this);
    }


    public void updateCurrentUser(){
        Message message = Message.obtain(customHandler);
        message.sendToTarget();
    }

    private void setCurrentUser(UserModel user){
        this.userModel = user;
        isLoaded.postValue(true);
    }

    public MutableLiveData<Boolean> getIsLoaded() {
        return isLoaded;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public List<VideoModel> fetchExploreVideos(){
        VideoModel test = new VideoModel("12356165456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/eypac8l0uvf5m9rtkqpf.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/eypac8l0uvf5m9rtkqpf.m3u8");
        VideoModel test1 = new VideoModel("11515623456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629684/smbexlxzhkwd3w5fk7aa.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/smbexlxzhkwd3w5fk7aa.m3u8");
        VideoModel test3 = new VideoModel("12332658489456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629957/utzhii6oujdtatmv5izr.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/utzhii6oujdtatmv5izr.m3u8");
        VideoModel test4 = new VideoModel("12356165456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/eypac8l0uvf5m9rtkqpf.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/eypac8l0uvf5m9rtkqpf.m3u8");
        VideoModel test5 = new VideoModel("11515623456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629684/smbexlxzhkwd3w5fk7aa.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/smbexlxzhkwd3w5fk7aa.m3u8");
        VideoModel test6 = new VideoModel("12332658489456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629957/utzhii6oujdtatmv5izr.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/utzhii6oujdtatmv5izr.m3u8");
        VideoModel test7 = new VideoModel("12356165456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/eypac8l0uvf5m9rtkqpf.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/eypac8l0uvf5m9rtkqpf.m3u8");
        VideoModel test8 = new VideoModel("11515623456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629684/smbexlxzhkwd3w5fk7aa.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/smbexlxzhkwd3w5fk7aa.m3u8");
        VideoModel test9 = new VideoModel("12332658489456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629957/utzhii6oujdtatmv5izr.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/utzhii6oujdtatmv5izr.m3u8");
        return Arrays.asList(test, test1, test3, test4, test5, test6, test7,test8,test9);
    }


    public void likeVideo(VideoModel videoModel){
        videoModel.setLike_count(1);
    }

    private static class MainActivityCustomHandler extends Handler {

        private UserDao userDao;
        private MainActivityViewModel mainActivityViewModel;

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
