package com.example.myapplication.Interface;

import androidx.fragment.app.Fragment;

public interface MainActivityContext {
    void openFragment(Fragment fragment, String tag);
    void setActiveBottomViewFragment(int position);
    void navigateToVideos(String video_url);
}
