package com.echoeyecodes.sinnerman.Adapters;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.sinnerman.Interface.ExploreFragmentContext
import com.echoeyecodes.sinnerman.Models.ExploreResponseBody
import com.echoeyecodes.sinnerman.Paging.CommonListPagingViewHolder
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.Utils.*
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import com.google.android.material.button.MaterialButton
import java.lang.Exception

typealias navigateToDestination = (String, String) -> Unit

class ExploreAdapter(private val exploreFragmentContext: ExploreFragmentContext, private val context:Context, private val navigateToMore: navigateToDestination, private val navigateToVideo: (String) -> Unit, itemCallback: DiffUtil.ItemCallback<Result<ExploreResponseBody>>) : ListAdapter<Result<ExploreResponseBody>, RecyclerView.ViewHolder>(itemCallback) {
    private val videosItemCallback = VideosItemCallback.newInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == LOADING_LAYOUT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.paging_loading_layout, parent, false);
            return CommonListPagingViewHolder(view, exploreFragmentContext)
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.explore_recycler_item, parent, false)
        return ExploreViewHolder(view)
    }

    companion object{
        const val LOADING_LAYOUT = 0
        const val NORMAL_LAYOUT = 1
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Result.Loading -> LOADING_LAYOUT
            is Result.Error -> LOADING_LAYOUT
            is Result.Success<*> -> NORMAL_LAYOUT
            else -> throw Exception("Invalid network state")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (getItem(position)) {
            is Result.Loading -> {
                val networkStateHolder = holder as CommonListPagingViewHolder
                networkStateHolder.handleNetworkStateChanged(NetworkState.LOADING)
            }
            is Result.Error -> {
                val networkStateHolder = holder as CommonListPagingViewHolder
                networkStateHolder.handleNetworkStateChanged(NetworkState.ERROR)
            }
            is Result.Success<ExploreResponseBody> -> {
                val viewHolder = holder as ExploreViewHolder
                val exploreResponseBody = (getItem(position) as Result.Success<ExploreResponseBody>).data

                viewHolder.recycler_header.text = exploreResponseBody.name
                val adapter = ExploreItemAdapter(videosItemCallback, context, navigateToVideo)
                viewHolder.recycler_view.adapter = adapter
                adapter.submitList(exploreResponseBody.videos)
                viewHolder.bindClickListener(exploreResponseBody.id, exploreResponseBody.name)

                if (exploreResponseBody.videos.size > 3) {
                    viewHolder.more_btn.visibility = View.VISIBLE
                } else {
                    viewHolder.more_btn.visibility = View.GONE
                }
            }
            else -> {
            }

        }
    }

    inner class ExploreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val recycler_header: TextView = itemView.findViewById(R.id.explore_recycler_item_text_view)
        val recycler_view: RecyclerView = itemView.findViewById(R.id.explore_recycler_item_recycler_view)
        val more_btn: MaterialButton = itemView.findViewById(R.id.explore_recycler_more_btn)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.view_item);

        init {

            recycler_view.setHasFixedSize(true)
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recycler_view.layoutManager = linearLayoutManager
            recycler_view.addItemDecoration(CustomItemDecoration(IntegerToDp.intToDp(10), IntegerToDp.intToDp(15), IntegerToDp.intToDp(10), IntegerToDp.intToDp(15)))
        }

        fun bindClickListener(key: String, value:String) {
            more_btn.setOnClickListener { navigateToMore(key, value) }
        }
    }
}
