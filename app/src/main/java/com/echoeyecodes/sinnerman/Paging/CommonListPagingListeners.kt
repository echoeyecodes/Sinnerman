package com.echoeyecodes.sinnerman.Paging

import com.echoeyecodes.sinnerman.viewmodel.NetworkState

interface CommonListPagingListeners{
    fun retry()
    fun onItemsChanged();
    fun onNetworkStateChanged() : NetworkState;
}