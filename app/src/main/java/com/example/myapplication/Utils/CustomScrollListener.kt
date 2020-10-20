package com.example.myapplication.Utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = (recyclerView.layoutManager) as LinearLayoutManager

        val totalItemCount = layoutManager.itemCount
        val visibleCount = layoutManager.childCount
        val firstItemPosition = layoutManager.findFirstVisibleItemPosition()

//        if(firstItemPosition > 0 && totalItemCount)

    }
}