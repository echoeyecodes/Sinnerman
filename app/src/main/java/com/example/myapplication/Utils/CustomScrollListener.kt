package com.example.myapplication.Utils

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomScrollListener(private val fetchMore: () -> Unit) : RecyclerView.OnScrollListener(){

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        if(newState == RecyclerView.SCROLL_STATE_IDLE){
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            val totalItems = layoutManager.itemCount
            val visibleItems = layoutManager.childCount
            val firstItemPosition = layoutManager.findFirstVisibleItemPosition()

            if((firstItemPosition + visibleItems) >= totalItems && firstItemPosition > 0){
                fetchMore.invoke()
            }
        }
    }
}