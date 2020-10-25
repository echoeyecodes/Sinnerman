package com.example.myapplication.Adapters;

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.lifecycle.*
import androidx.recyclerview.widget.*
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.ExploreResponseBody
import com.example.myapplication.R
import com.example.myapplication.Utils.*
import com.google.android.material.button.MaterialButton
import kotlin.collections.HashMap

typealias navigateToDestination = (String) -> Unit
class ExploreAdapter(private val context: Context, private val navigateToMore: navigateToDestination, private val navigateToVideo: navigateToDestination, itemCallback: DiffUtil.ItemCallback<ExploreResponseBody>) : ListAdapter<ExploreResponseBody, ExploreAdapter.ExploreViewHolder>(itemCallback) {
    private val videosItemCallback = VideosItemCallback.newInstance()
    private var recycler_states : HashMap<String, Parcelable>? = null
    private val videoDao : VideosDao = ApiClient.getInstance(context).getClient(VideosDao::class.java)
    private val viewPool = RecyclerView.RecycledViewPool()


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
        return ExploreViewHolder(view)
    }

    override fun onBindViewHolder( holder : ExploreViewHolder, position : Int) {
        val exploreResponseBody = getItem(position)

        if(exploreResponseBody != null){
            holder.recycler_header.text = exploreResponseBody.name

            val adapter = ExploreItemAdapter(videosItemCallback, context, navigateToVideo)
            holder.recycler_view.adapter = adapter
            adapter.submitList(exploreResponseBody.videos)
            holder.bindClickListener(exploreResponseBody.id)

            if(exploreResponseBody.videos.size > 3){
                holder.more_btn.visibility = View.VISIBLE
            }else{
                holder.more_btn.visibility = View.GONE
            }
        }
    }

    inner class ExploreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         val recycler_header: TextView = itemView.findViewById(R.id.explore_recycler_item_text_view)
         val recycler_view: RecyclerView = itemView.findViewById(R.id.explore_recycler_item_recycler_view)
         val more_btn : MaterialButton = itemView.findViewById(R.id.explore_recycler_more_btn)

        init {

            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recycler_view.layoutManager = linearLayoutManager

            recycler_view.setHasFixedSize(true)
            recycler_view.addItemDecoration(CustomItemDecoration(IntegerToDp.intToDp(10), IntegerToDp.intToDp(15),IntegerToDp.intToDp(5),IntegerToDp.intToDp(15)))
            recycler_view.setRecycledViewPool(viewPool)
        }

        fun bindClickListener(key:String) {
            more_btn.setOnClickListener { navigateToMore(key) }
        }

    }
}
