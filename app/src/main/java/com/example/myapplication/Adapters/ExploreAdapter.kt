package com.example.myapplication.Adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.lifecycle.*
import androidx.paging.*
import androidx.recyclerview.widget.*
import com.example.myapplication.API.ApiUtils.ApiClient
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.ExploreResponseBody
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.Paging.ExploreItemPagingSource
import com.example.myapplication.Paging.VideoPagingSource
import com.example.myapplication.R;
import com.example.myapplication.Utils.HorizontalItemDecoration;
import com.example.myapplication.Utils.VideosItemCallback
import com.example.myapplication.viewmodel.ExploreViewModel
import kotlin.collections.HashMap
import kotlin.reflect.KFunction1

typealias navigateToDestination = (String) -> Unit
class ExploreAdapter(private val context: Context, private val lifecycleOwner: LifecycleOwner, private val navigateToMore: navigateToDestination, private val navigateToVideo: navigateToDestination, itemCallback: DiffUtil.ItemCallback<ExploreResponseBody>) : PagingDataAdapter<ExploreResponseBody, ExploreAdapter.ExploreViewHolder>(itemCallback) {
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
            holder.recycler_view.setHasFixedSize(true)
            holder.recycler_view.adapter = adapter
            loadItemData(exploreResponseBody.videos, exploreResponseBody.name).observe(lifecycleOwner, Observer<PagingData<VideoResponseBody>>{
                adapter.submitData(lifecycleOwner.lifecycle, it)
            })
            holder.bindClickListener(exploreResponseBody.id)
        }
    }

    private fun loadItemData(videos: List<VideoResponseBody>, key: String) :LiveData<PagingData<VideoResponseBody>> {
        return Pager(
                config = PagingConfig(
                        pageSize = videos.size,
                        enablePlaceholders = false,
                        maxSize = 25
                ),
                pagingSourceFactory = { ExploreItemPagingSource(videoDao, key, videos) }
        ).liveData.cachedIn(lifecycleOwner.lifecycle)
    }


    inner class ExploreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

         val recycler_header: TextView = itemView.findViewById(R.id.explore_recycler_item_text_view)
         val recycler_view: RecyclerView = itemView.findViewById(R.id.explore_recycler_item_recycler_view)
         val imageButton : ImageButton = itemView.findViewById(R.id.explore_recycler_more_btn)

        init {
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(recycler_view)

            val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            recycler_view.layoutManager = linearLayoutManager

            val horizontalItemDecoration = HorizontalItemDecoration()
            recycler_view.addItemDecoration(horizontalItemDecoration)
            recycler_view.setRecycledViewPool(viewPool)
        }

        fun bindClickListener(key:String) {
            imageButton.setOnClickListener { navigateToMore(key) }
        }

    }
}
