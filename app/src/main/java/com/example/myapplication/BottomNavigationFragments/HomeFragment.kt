package com.example.myapplication.BottomNavigationFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.R
import com.example.myapplication.RootBottomFragment
import com.example.myapplication.Utils.*
import com.example.myapplication.viewmodel.BottomFragmentViewModel.HomeFragmentViewModel
import com.example.myapplication.viewmodel.NetworkState
import com.google.android.material.chip.Chip


class HomeFragment : RootBottomFragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var recyclerView: RecyclerView
    private var adapter: HomeFragmentRecyclerViewAdapter? = null
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var linearLayoutManager: LinearLayoutManager


    init {
        TAG = "HOME_FRAGMENT"
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(HomeFragmentViewModel::class.java)
        swipeRefreshLayout = view.findViewById(R.id.home_swipe_refresh)

        recyclerView = view.findViewById(R.id.fragment_home_recycler_view)

        swipeRefreshLayout.setOnRefreshListener(this)

        val videosItemCallback = VideosItemCallback.newInstance()

        adapter = HomeFragmentRecyclerViewAdapter(videosItemCallback, context, this)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addItemDecoration(CustomItemDecoration(IntegerToDp.intToDp(15), IntegerToDp.intToDp(15)))

        recyclerView.adapter = adapter

        viewModel.getVideos().observe(viewLifecycleOwner, Observer<List<VideoResponseBody>> { videos ->
            adapter?.submitList(videos)
        })

        viewModel.networkStatus.observe(viewLifecycleOwner, Observer<NetworkState> { state ->
            if(state == NetworkState.LOADING || state == NetworkState.ERROR){
                val originalList = ArrayList<VideoResponseBody?>(viewModel.state)
                originalList.add(null)
                adapter?.submitList(originalList)
                adapter?.onNetworkStateChanged(state)
            }
            swipeRefreshLayout.isRefreshing = state == NetworkState.REFRESHING
        })

        recyclerView.addOnScrollListener(CustomScrollListener(fetchMore = this::fetchMore))
    }

    fun fetchMore(){
        viewModel.fetchMore(NetworkState.LOADING);
    }

    override fun onResume() {
        super.onResume()
        mainActivityContext.setActiveBottomViewFragment(0)
    }
    fun retry(){
        viewModel.retry()
    }

    override fun onRefresh() {
        viewModel.refresh()
    }
}
