package com.echoeyecodes.sinnerman.Interface;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.echoeyecodes.sinnerman.CustomFragment;
import com.echoeyecodes.sinnerman.DrawerFragments;
import com.echoeyecodes.sinnerman.Models.VideoResponseBody;
import com.echoeyecodes.sinnerman.RootBottomFragment

interface MainActivityContext {

    fun openFragment( fragment:DrawerFragments, item : MenuItem?);
    fun navigateToVideos(video_url : String)
    fun openExternalLink (link:String);
    fun onOptionSelected(video : VideoResponseBody, position :Int);
    fun onThemeChange(value : Boolean);
    fun onDrawerFragmentActive(fragments : DrawerFragments);
    fun openBottomFragment(fragment: RootBottomFragment, tag: String)
    fun setActiveBottomViewFragment(position: Int)
}
