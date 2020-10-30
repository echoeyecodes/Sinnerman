package com.echoeyecodes.sinnerman.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.sinnerman.R

class VideoLoadStateAdapter : LoadStateAdapter<VideoLoadStateAdapter.VideoLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: VideoLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): VideoLoadStateViewHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_footer, parent, false)
        return VideoLoadStateViewHolder(view)
    }

    inner class VideoLoadStateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(loadState: LoadState){
            if(loadState is LoadState.Loading){
                itemView.visibility = View.VISIBLE
            }else{
                itemView.visibility = View.GONE
            }
        }
    }

}