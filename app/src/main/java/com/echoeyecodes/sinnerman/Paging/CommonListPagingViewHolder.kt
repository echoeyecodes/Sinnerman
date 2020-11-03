package com.echoeyecodes.sinnerman.Paging

import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import com.google.android.material.button.MaterialButton

open class CommonListPagingViewHolder(itemView: View, listener: CommonListPagingListeners) : RecyclerView.ViewHolder(itemView) {

    //Loading layout
    open val loading_container = itemView.findViewById<RelativeLayout>(R.id.loading_layout)
    val loading_error_container = itemView.findViewById<LinearLayout>(R.id.loading_error_container)
    val progress_bar = itemView.findViewById<ProgressBar>(R.id.loading_progress_bar)
    val retry_btn: MaterialButton = itemView.findViewById(R.id.retry_btn)

    init {
        retry_btn.setOnClickListener {listener.retry() }
    }

    fun handleNetworkStateChanged(networkState: NetworkState) {
        loading_container.visibility = View.VISIBLE
        when (networkState) {
            NetworkState.LOADING -> {
                loading_error_container.visibility = View.GONE
                progress_bar.visibility = View.VISIBLE
            }
            NetworkState.ERROR -> {
                progress_bar.visibility = View.GONE
                loading_error_container.visibility = View.VISIBLE
            }
            else -> {
                loading_container.visibility = View.GONE
                loading_error_container.visibility = View.GONE
            }
        }
    }

}