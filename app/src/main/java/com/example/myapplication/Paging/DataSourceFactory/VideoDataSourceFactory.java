package com.example.myapplication.Paging.DataSourceFactory;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import com.example.myapplication.Paging.DataSource.VideoDataSource;

import java.util.List;

public class VideoDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<VideoDataSource> videoDataSourceMutableLiveData;
    private VideoDataSource videoDataSource;
    private Context context;

    public VideoDataSourceFactory(Context context){
        videoDataSourceMutableLiveData = new MutableLiveData<>();
        this.context = context;
    }

    @NonNull
    @Override
    public DataSource create() {
        videoDataSource = new VideoDataSource(context);
        videoDataSourceMutableLiveData.postValue(videoDataSource);
        return videoDataSource;
    }

    public MutableLiveData<VideoDataSource> getVideoDataSourceMutableLiveData() {
        return videoDataSourceMutableLiveData;
    }
}
