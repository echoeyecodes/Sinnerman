package com.example.myapplication.Interface;

import com.example.myapplication.Models.VideoModel;

public interface MainActivityContext {
    void openComments();
    void openVideoFragment(String video_url);
    void openYourVideosFragment();
    void toggleFullScreen(boolean value);

    void likeVideo(VideoModel videoModel);
}
