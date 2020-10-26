package com.example.myapplication.BottomNavigationFragments

import android.content.Intent
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
import com.example.myapplication.Activities.VideoListActivity
import com.example.myapplication.Adapters.ExploreAdapter
import com.example.myapplication.Interface.ExploreFragmentContext
import com.example.myapplication.MainActivity
import com.example.myapplication.Models.ExploreResponseBody
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.R
import com.example.myapplication.RootBottomFragment
import com.example.myapplication.Utils.CustomScrollListener
import com.example.myapplication.viewmodel.ExploreViewModel
import com.example.myapplication.viewmodel.NetworkState


class ExploreFragment : RootBottomFragment(), ExploreFragmentContext, SwipeRefreshLayout.OnRefreshListener{
    private lateinit var recyclerView : RecyclerView
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var exploreViewModel: ExploreViewModel
    private lateinit var exploreAdapter : ExploreAdapter
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout

    init{
        TAG ="EXPLORE_FRAGMENT"
    }

    companion object{
        fun newInstance(): ExploreFragment = ExploreFragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainActivity: MainActivity = context as MainActivity
        recyclerView = view.findViewById(R.id.explore_recycler_view)
        swipeRefreshLayout = view.findViewById(R.id.explore_swipe_refresh)

        swipeRefreshLayout.setOnRefreshListener(this)
        exploreViewModel = ViewModelProvider(requireActivity()).get(ExploreViewModel::class.java)


        linearLayoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = linearLayoutManager
        exploreAdapter = ExploreAdapter(
                exploreFragment = this,
                navigateToMore =  this::navigateToVideoListActivity,
                navigateToVideo = mainActivity::navigateToVideos,
                itemCallback = ExploreResponseItemCallback())

        recyclerView.adapter = exploreAdapter

        exploreViewModel.categories.observe(viewLifecycleOwner, Observer<List<ExploreResponseBody>> { value ->
            exploreAdapter.submitList(value)
        })

        exploreViewModel.networkStatus.observe(viewLifecycleOwner, Observer<NetworkState> { state ->
            if(state == NetworkState.LOADING || state == NetworkState.ERROR){
                val originalList = ArrayList<ExploreResponseBody?>(exploreViewModel.state)
                originalList.add(null)
                exploreAdapter.submitList(originalList)
                exploreAdapter.onNetworkStateChanged(state)
            }
            swipeRefreshLayout.isRefreshing = state == NetworkState.REFRESHING
        })

        recyclerView.addOnScrollListener(CustomScrollListener(fetchMore = this::fetchMore))
    }

    inner class ExploreResponseItemCallback : DiffUtil.ItemCallback<ExploreResponseBody>() {

        override fun areItemsTheSame(oldItem: ExploreResponseBody, newItem: ExploreResponseBody): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExploreResponseBody, newItem: ExploreResponseBody): Boolean {
            return oldItem == newItem
        }

    }

    fun fetchMore(){
        exploreViewModel.fetchMore(NetworkState.LOADING);
    }

    override fun onResume() {
        super.onResume()
        mainActivityContext.setActiveBottomViewFragment(1)
    }

    fun retry(){
        exploreViewModel.retry()
    }

    override fun navigateToVideoListActivity(title: String?) {
        val intent =Intent(context, VideoListActivity::class.java)
        intent.putExtra("title", title)
        startActivity(intent)
    }

    override fun onRefresh() {
        exploreViewModel.refresh()
    }

}