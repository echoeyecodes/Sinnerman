package com.example.myapplication.ViewModel;

import android.app.Application;

import android.os.Message;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.API.DAO.ApiClient;
import com.example.myapplication.API.DAO.VideosDao;
import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.Models.VideoResponseBody;
import com.example.myapplication.Utils.VideoThreadHandler;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {


    public MainActivityViewModel(@NonNull Application application) {
        super(application);

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


}
