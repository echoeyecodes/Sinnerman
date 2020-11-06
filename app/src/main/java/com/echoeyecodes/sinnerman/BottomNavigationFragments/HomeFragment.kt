package com.echoeyecodes.sinnerman.BottomNavigationFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.echoeyecodes.sinnerman.Adapters.HomeFragmentRecyclerViewAdapter
import com.echoeyecodes.sinnerman.Interface.HomeFragmentListener
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.RootBottomFragment
import com.echoeyecodes.sinnerman.Utils.*
import com.echoeyecodes.sinnerman.viewmodel.BottomFragmentViewModel.HomeFragmentViewModel
import com.echoeyecodes.sinnerman.viewmodel.NetworkState


class HomeFragment : RootBottomFragment(), SwipeRefreshLayout.OnRefreshListener, HomeFragmentListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeFragmentRecyclerViewAdapter
    private lateinit var viewModel: HomeFragmentViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var linearLayoutManager: LinearLayoutManager


    init {
        TAG = "HOME_FRAGMENT"
    }

    companion object {
        fun newInstance(): HomeFragment = HomeFragment()
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

        adapter = HomeFragmentRecyclerViewAdapter(SealedClassDiffUtil(), requireContext(),this)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addItemDecoration(CustomItemDecoration(IntegerToDp.intToDp(15), IntegerToDp.intToDp(15)))

        recyclerView.adapter = adapter

        viewModel.getVideos().observe(viewLifecycleOwner, Observer<List<VideoResponseBody>> { videos ->
                val currentState = viewModel.networkStatus.value

                if(currentState == Result.Idle){
                val items = videos.map { Result.Success(it) }
                adapter.submitList(items)
            }
        })


        viewModel.networkStatus.observe(viewLifecycleOwner, Observer<Result<VideoResponseBody>> { state ->
            when (state) {
                is Result.Loading -> {
                    val originalItems = ArrayList(viewModel.state.map { Result.Success(it) }) + Result.Loading
                    adapter.submitList(originalItems)
                }
                is Result.Error -> {
                    val originalItems = ArrayList(viewModel.state.map { Result.Success(it) }) + Result.Error
                    adapter.submitList(originalItems)
                }
                is Result.Refreshing -> swipeRefreshLayout.isRefreshing = true
                else -> {}
            }

            swipeRefreshLayout.isRefreshing = state == Result.Refreshing
        })

        recyclerView.addOnScrollListener(CustomScrollListener(fetchMore = this::fetchMore))
    }

    fun fetchMore(){
        viewModel.fetchMore(Result.Loading);
    }

    override fun onResume() {
        super.onResume()
        mainActivityContext.setActiveBottomViewFragment(0)
    }
    override fun retry(){
        viewModel.retry()
    }

    override fun onItemsChanged() {

    }

    override fun onNetworkStateChanged(): NetworkState {
        return NetworkState.ERROR
    }

    override fun onRefresh() {
        viewModel.refresh()
    }
}
