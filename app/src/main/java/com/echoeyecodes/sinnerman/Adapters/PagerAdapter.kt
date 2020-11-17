package com.echoeyecodes.sinnerman.Adapters

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.sinnerman.Models.PagerModel
import com.echoeyecodes.sinnerman.Paging.CommonListPagingViewHolder
import com.echoeyecodes.sinnerman.Utils.Result
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import java.lang.Exception

abstract class PagerAdapter<T : PagerModel> (itemCallback: DiffUtil.ItemCallback<Result<T>>) : ListAdapter<Result<T>, RecyclerView.ViewHolder>(itemCallback) {


    companion object{
        const val LOADING_LAYOUT = 0
        const val NORMAL_LAYOUT = 1
        const val ADS_LAYOUT = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Result.Loading, Result.Error -> LOADING_LAYOUT
            is Result.Success<*> -> NORMAL_LAYOUT
            else -> throw Exception("Invalid network state")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItem(position)) {
            is Result.Loading, Result.Error -> {
                val networkStateHolder = holder as CommonListPagingViewHolder
                val displayMetrics = Resources.getSystem().displayMetrics;

                val view = networkStateHolder.itemView
                val layoutParams = view.layoutParams as RecyclerView.LayoutParams
                if (itemCount == 1) {
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                } else {
                    layoutParams.height = (displayMetrics.heightPixels / 3.5).toInt();
                }
                view.layoutParams = layoutParams
            }
        }

        when (getItem(position)) {
            is Result.Loading -> {
                val networkStateHolder = holder as CommonListPagingViewHolder
                networkStateHolder.handleNetworkStateChanged(NetworkState.LOADING)
            }
            is Result.Error -> {
                val networkStateHolder = holder as CommonListPagingViewHolder
                networkStateHolder.handleNetworkStateChanged(NetworkState.ERROR)
            }
        }
    }
}