package com.echoeyecodes.sinnerman.Interface

import com.echoeyecodes.sinnerman.RootBottomFragment

interface PrimaryFragmentContext {

    fun openBottomFragment(fragment: RootBottomFragment, tag: String)
    fun setActiveBottomViewFragment(position: Int)
    fun navigateToVideos(video_url: String)

}