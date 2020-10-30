package com.echoeyecodes.sinnerman.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.echoeyecodes.sinnerman.Adapters.HomeFragmentRecyclerViewAdapter
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.Interface.PagingListener
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.Utils.CustomItemDecoration
import com.echoeyecodes.sinnerman.Utils.CustomScrollListener
import com.echoeyecodes.sinnerman.Utils.IntegerToDp
import com.echoeyecodes.sinnerman.Utils.VideosItemCallback
import com.echoeyecodes.sinnerman.viewmodel.NetworkState
import com.echoeyecodes.sinnerman.viewmodel.VideoListViewModel

class VideoListActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener, MainActivityContext, PagingListener{
    private lateinit var toolbar: LinearLayout
    private lateinit var toolbar_text: TextView
    private lateinit var toolbar_back_btn: ImageButton
    private lateinit var recyclerView: RecyclerView
    private var adapter: HomeFragmentRecyclerViewAdapter? = null
    private lateinit var viewModel: VideoListViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var linearLayoutManager: LinearLayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_list)
        toolbar = findViewById(R.id.video_list_toolbar)
        toolbar_text = toolbar.findViewById(R.id.sub_activity_title)
        toolbar_back_btn = toolbar.findViewById(R.id.sub_activity_back_btn)
        val title = intent.getStringExtra("title")
        toolbar_text.text = title
        toolbar_back_btn.setOnClickListener { super.onBackPressed() }

        viewModel = ViewModelProvider(this).get(VideoListViewModel::class.java)
        viewModel.tag_id = title!!



        swipeRefreshLayout = findViewById(R.id.home_swipe_refresh)
        recyclerView = findViewById(R.id.video_list_recycler_view)

        swipeRefreshLayout.setOnRefreshListener(this)

        val videosItemCallback = VideosItemCallback.newInstance()

        adapter = HomeFragmentRecyclerViewAdapter(videosItemCallback, this, this)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addItemDecoration(CustomItemDecoration(IntegerToDp.intToDp(15), IntegerToDp.intToDp(15)))

        recyclerView.adapter = adapter

        viewModel.videos.observe(this, Observer<List<VideoResponseBody>> { videos ->
            adapter?.submitList(videos)
        })

        viewModel.networkStatus.observe(this, Observer<NetworkState> { state ->
            if(state == NetworkState.LOADING || state == NetworkState.ERROR){
                val originalList = ArrayList<VideoResponseBody?>(viewModel.state)
                originalList.add(null)
                adapter?.let{
                    it.submitList(originalList)

                    //necessary call to force notification update
                    // due to the diffutil.callback comparison when
                    //the state changes from loading to error or vice-versa
                    it.notifyItemChanged(it.itemCount - 1)
                    it.onNetworkStateChanged(state)
                }
            }
            swipeRefreshLayout.isRefreshing = state == NetworkState.REFRESHING
        })

        recyclerView.addOnScrollListener(CustomScrollListener(fetchMore = this::fetchMore))
    }

    fun fetchMore(){
        viewModel.fetchMore(NetworkState.LOADING);
    }

    override fun retry(){
        viewModel.retry()
    }

    override fun onRefresh() {
        viewModel.refresh()
    }

    override fun navigateToVideos(video_url: String?) {
        val intent = Intent(this, VideoActivity::class.java)
        intent.putExtra("video_id", video_url)
        startActivity(intent)
    }

    override fun openFragment(fragment: Fragment?, tag: String?) {
        TODO("Not yet implemented")
    }

    override fun setActiveBottomViewFragment(position: Int) {
        TODO("Not yet implemented")
    }
}