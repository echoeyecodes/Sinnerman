package com.echoeyecodes.sinnerman.Interface;

import com.echoeyecodes.sinnerman.Paging.CommonListPagingListeners;

public interface ExploreFragmentContext extends CommonListPagingListeners {

    void navigateToVideoListActivity(String key, String title);
}
