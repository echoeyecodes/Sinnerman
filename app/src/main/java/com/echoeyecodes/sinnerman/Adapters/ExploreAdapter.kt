package com.echoeyecodes.sinnerman.Adapters;

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.sinnerman.BottomNavigationFragments.ExploreFragment
import com.echoeyecodes.sinnerman.Interface.ExploreFragmentContext
import com.echoeyecodes.sinnerman.Models.ExploreResponseBody
import com.echoeyecodes.sinnerman.Paging.CommonListPagingListeners
import com.echoeyecodes.sinnerman.Paging.CommonListPagingViewHolder
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.Utils.CustomItemDecoration
import com.echoeyecodes.sinnerman.Utils.IntegerToDp
import com.echoeyecodes.sinnerman.Utils.VideosItemCallback
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import com.google.android.material.button.MaterialButton

typealias navigateToDestination = (String, String) -> Unit

class ExploreAdapter(private val exploreFragmentContext: ExploreFragmentContext, private val context:Context, private val navigateToMore: navigateToDestination, private val navigateToVideo: (String) -> Unit, itemCallback: DiffUtil.ItemCallback<ExploreResponseBody>) : ListAdapter<ExploreResponseBody, ExploreAdapter.ExploreViewHolder>(itemCallback) {
    private val videosItemCallback = VideosItemCallback.newInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExploreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.explore_recycler_item, parent, false)
        return ExploreViewHolder(view)
    }


    override fun onBindViewHolder(holder: ExploreViewHolder, position: Int) {
        val exploreResponseBody = getItem(position)

        if (exploreResponseBody == null) {
            holder.linearLayout.visibility = View.GONE
            holder.handleNetworkStateChanged(exploreFragmentContext.onNetworkStateChanged())
            return
        }
        holder.linearLayout.visibility = View.VISIBLE
        holder.loading_container.visibility = View.GONE
        holder.recycler_header.text = exploreResponseBody.name
        val adapter = ExploreItemAdapter(videosItemCallback, context, navigateToVideo)
        holder.recycler_view.adapter = adapter
        adapter.submitList(exploreResponseBody.videos)
        holder.bindClickListener(exploreResponseBody.id, exploreResponseBody.name)

        if (exploreResponseBody.videos.size > 3) {
            holder.more_btn.visibility = View.VISIBLE
        } else {
            holder.more_btn.visibility = View.GONE
        }
    }

    inner class ExploreViewHolder(itemView: View) : CommonListPagingViewHolder(itemView, exploreFragmentContext) {

        val recycler_header: TextView = itemView.findViewById(R.id.explore_recycler_item_text_view)
        val recycler_view: RecyclerView = itemView.findViewById(R.id.explore_recycler_item_recycler_view)
        val more_btn: MaterialButton = itemView.findViewById(R.id.explore_recycler_more_btn)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.view_item);

        init {

            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recycler_view.layoutManager = linearLayoutManager
            recycler_view.addItemDecoration(CustomItemDecoration(IntegerToDp.intToDp(10), IntegerToDp.intToDp(15), IntegerToDp.intToDp(10), IntegerToDp.intToDp(15)))
        }

        fun bindClickListener(key: String, value:String) {
            more_btn.setOnClickListener { navigateToMore(key, value) }
        }
    }
}
