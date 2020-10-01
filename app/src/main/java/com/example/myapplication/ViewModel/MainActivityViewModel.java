package com.example.myapplication.ViewModel;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.myapplication.Models.MovieModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MainActivityViewModel extends ViewModel {
    private MutableLiveData<List<MovieModel>> videos = new MutableLiveData<>();

    public MainActivityViewModel(){
        videos.setValue(fetchVideos());
    }

    private List<MovieModel> fetchVideos(){
        MovieModel test = new MovieModel("12356165456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1600686480/xf6knnaxf6yca9lzgz7d.m3u8");
        MovieModel test1 = new MovieModel("11515623456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1600686482/eib6rz0mzjlqw0rp1vfc.m3u8");
        MovieModel test3 = new MovieModel("12332658489456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1600686490/dzikdq9z8gfs6x40osyt.m3u8");
        return Arrays.asList(test, test1, test3);
    }

    public MutableLiveData<List<MovieModel>> getVideos() {
        return videos;
    }

    public void likeVideo(MovieModel movieModel){
        movieModel.setLike_count(1);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        videos = null;
    }
}
