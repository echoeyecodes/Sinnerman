package com.example.myapplication.Interface;

import androidx.fragment.app.Fragment;
import com.example.myapplication.Models.VideoModel;

public interface MainActivityContext {
    void openFragment(Fragment fragment);
    void openYourVideosFragment();
    void navigateToVideos(String video_url);
}
