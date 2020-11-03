package com.echoeyecodes.sinnerman.Interface;

import androidx.fragment.app.Fragment;
import com.echoeyecodes.sinnerman.Models.VideoResponseBody;

public interface MainActivityContext {
    void openFragment(Fragment fragment, String tag);
    void setActiveBottomViewFragment(int position);
    void navigateToVideos(String video_url);
    void onOptionSelected(VideoResponseBody video, int position);
}
