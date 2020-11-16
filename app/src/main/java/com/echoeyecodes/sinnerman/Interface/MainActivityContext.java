package com.echoeyecodes.sinnerman.Interface;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.echoeyecodes.sinnerman.CustomFragment;
import com.echoeyecodes.sinnerman.DrawerFragments;
import com.echoeyecodes.sinnerman.Models.VideoResponseBody;

public interface MainActivityContext {
    void openFragment(DrawerFragments fragment);
    void navigateToVideos(String video_url);
    void onOptionSelected(VideoResponseBody video, int position);
    void onCategorySelected(int position);
    void onDrawerFragmentActive(DrawerFragments fragments);
}
