package com.example.myapplication.Interface;

import com.example.myapplication.Models.VideoModel;

public interface MainActivityContext {
    void openVideoFragment(String video_url);
    void openYourVideosFragment();
    void toggleFullScreen(boolean value);
    void hideStatusBarAndNavigation();
    void navigateToComments();
}
