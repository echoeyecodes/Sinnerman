package com.example.myapplication.Interface;

import androidx.fragment.app.Fragment;
import com.example.myapplication.Models.VideoResponseBody;

public interface MainActivityContext {
    void openFragment(Fragment fragment, String tag);
    void setActiveBottomViewFragment(int position);
    void navigateToVideos(String video_url);
}
