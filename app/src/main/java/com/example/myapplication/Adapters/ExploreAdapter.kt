package com.example.myapplication.Adapters;

import android.content.Context;
import android.os.Parcelable;
import android.util.Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.*
import com.example.myapplication.Models.ExploreResponseBody
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.R;
import com.example.myapplication.Utils.HorizontalItemDecoration;
import com.example.myapplication.Utils.VideosItemCallback
import com.example.myapplication.viewmodel.ExploreViewModel
import kotlin.collections.HashMap

typealias navigateToDestination = (String) -> Unit
class ExploreAdapter(private val context: Context, private val navigateToMore: navigateToDestination, private val navigateToVideo: navigateToDestination, itemCallback: DiffUtil.ItemCallback<ExploreResponseBody>) : PagingDataAdapter<ExploreResponseBody, ExploreAdapter.ExploreViewHolder>(itemCallback) {

    private val videosItemCallback = VideosItemCallback.newInstance()
    private var recycler_states : HashMap<String, Parcelable>? = null


    override fun onViewDetachedFromWindow(holder : ExploreViewHolder) {
        super.onViewDetachedFromWindow(holder)

        val linearLayoutManager =  holder.recycler_view.layoutManager as LinearLayoutManager
        linearLayoutManager.onSaveInstanceState()?.let { recycler_states?.put(holder.recycler_header.text.toString(), it) }
    }

    fun resetRecyclerStates(){
        recycler_states = null
    }

    override fun onViewAttachedToWindow( holder : ExploreViewHolder) {
        super.onViewAttachedToWindow(holder)
        val linearLayoutManager : LinearLayoutManager = holder.recycler_view.layoutManager as LinearLayoutManager
        linearLayoutManager.onRestoreInstanceState(recycler_states?.get(holder.recycler_header.text.toString()))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType : Int): ExploreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.explore_recycler_item, parent, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.explore_recycler_item_recycler_view)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager

        val horizontalItemDecoration = HorizontalItemDecoration()
        recyclerView.addItemDecoration(horizontalItemDecoration)
        return ExploreViewHolder(view)
    }

    override fun onBindViewHolder( holder : ExploreViewHolder, position : Int) {
        val exploreResponseBody = getItem(position)

        if(exploreResponseBody != null){
            holder.recycler_header.text = exploreResponseBody.name

            val adapter = ExploreItemAdapter(videosItemCallback, navigateToVideo);
            holder.recycler_view.adapter = adapter
            adapter.submitList(exploreResponseBody.videos)

            holder.bindClickListener(exploreResponseBody.id)
        }
    }

    inner class ExploreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         val recycler_header: TextView = itemView.findViewById(R.id.explore_recycler_item_text_view)
         val recycler_view: RecyclerView = itemView.findViewById(R.id.explore_recycler_item_recycler_view)
         val imageButton : ImageButton = itemView.findViewById(R.id.explore_recycler_more_btn)

        fun bindClickListener(key:String) {
            imageButton.setOnClickListener { navigateToMore(key) }
        }

    }
}
