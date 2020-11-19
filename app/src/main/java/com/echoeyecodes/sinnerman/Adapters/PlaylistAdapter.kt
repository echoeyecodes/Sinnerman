package com.echoeyecodes.sinnerman.Adapters;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.echoeyecodes.sinnerman.Interface.ExploreFragmentContext
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.Models.ExploreResponseBody
import com.echoeyecodes.sinnerman.Paging.CommonListPagingViewHolder
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.Utils.*
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import com.google.android.material.button.MaterialButton
import java.lang.Exception


class PlaylistAdapter(private val exploreFragmentContext: ExploreFragmentContext, private val context:Context, private val navigateToMore: navigateToDestination, private val mainActivityContext: MainActivityContext, itemCallback: DiffUtil.ItemCallback<Result<ExploreResponseBody>>) : PagerAdapter<ExploreResponseBody>(itemCallback) {
    private val videosItemCallback = VideosItemCallback.newInstance()

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == LOADING_LAYOUT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.paging_loading_layout, parent, false);
            return CommonListPagingViewHolder(view, exploreFragmentContext)
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_playlist_recycler_item, parent, false)
        return PlaylistViewHolder(view)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (getItem(position)) {
            is Result.Success<ExploreResponseBody> -> {
                val viewHolder = holder as PlaylistViewHolder
                val exploreResponseBody = (getItem(position) as Result.Success<ExploreResponseBody>).data

                viewHolder.recycler_header.text = exploreResponseBody.name
                val adapter = PlaylistItemAdapter(videosItemCallback, context, mainActivityContext)
                viewHolder.recycler_view.adapter = adapter
                adapter.submitList(exploreResponseBody.videos)
                viewHolder.bindClickListener(exploreResponseBody.id, exploreResponseBody.name)

            }
            else -> {
            }

        }
    }

    inner class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val recycler_header: TextView = itemView.findViewById(R.id.explore_recycler_item_text_view)
        val recycler_view: RecyclerView = itemView.findViewById(R.id.playlist_recycler_item_recycler_view)
        val more_btn: ImageButton = itemView.findViewById(R.id.explore_recycler_more_btn)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.view_item);

        init {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(recycler_view)
            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recycler_view.layoutManager = linearLayoutManager
            recycler_view.addItemDecoration(HorizontalItemDecoration())
        }

        fun bindClickListener(key: String, value:String) {
            more_btn.setOnClickListener { navigateToMore(key, value) }
        }
    }
}
