package com.echoeyecodes.sinnerman.Interface;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.echoeyecodes.sinnerman.CustomFragment;
import com.echoeyecodes.sinnerman.DrawerFragments;
import com.echoeyecodes.sinnerman.Models.VideoResponseBody;

public interface MainActivityContext {
    void openFragment(DrawerFragments fragment, @Nullable MenuItem item);
    void navigateToVideos(String video_url);
    void openExternalLink(String link);
    void onOptionSelected(VideoResponseBody video, int position);
    void onCategorySelected(int position);
    void onDrawerFragmentActive(DrawerFragments fragments);
}
