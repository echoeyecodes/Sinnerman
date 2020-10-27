package com.example.myapplication.Paging

import com.example.myapplication.viewmodel.NetworkState

interface CommonListPagingListeners{
    fun retry()

    fun onNetworkStateChanged(networkState: NetworkState);
}