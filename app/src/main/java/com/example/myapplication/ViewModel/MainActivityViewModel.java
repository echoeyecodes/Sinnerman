package com.example.myapplication.ViewModel;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.Models.VideoModel;

import java.util.Arrays;
import java.util.List;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<List<VideoModel>> videos = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFullScreen = new MutableLiveData<>();

    public MainActivityViewModel(){
        videos.setValue(fetchVideos());
        isFullScreen.setValue(false);
    }

    private List<VideoModel> fetchVideos(){
        VideoModel test = new VideoModel("12356165456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/eypac8l0uvf5m9rtkqpf.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/eypac8l0uvf5m9rtkqpf.m3u8");
        VideoModel test1 = new VideoModel("11515623456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629684/smbexlxzhkwd3w5fk7aa.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/smbexlxzhkwd3w5fk7aa.m3u8");
        VideoModel test3 = new VideoModel("12332658489456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629957/utzhii6oujdtatmv5izr.jpg", "https://res.cloudinary.com/echoeyecodes/video/upload/v1601629562/utzhii6oujdtatmv5izr.m3u8");
        return Arrays.asList(test, test1, test3);
    }

    public MutableLiveData<List<VideoModel>> getVideos() {
        return videos;
    }

    public void likeVideo(VideoModel videoModel){
        videoModel.setLike_count(1);
    }

    public MutableLiveData<Boolean> getIsFullScreen() {
        return isFullScreen;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        videos = null;
    }

    public boolean getIsFullScreenValue(){
        assert isFullScreen.getValue() != null;
        return isFullScreen.getValue();
    }

    public void toggleFullScreen(boolean value){
        isFullScreen.setValue(value);
    }
}
