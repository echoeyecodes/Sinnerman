package com.echoeyecodes.sinnerman.BottomNavigationFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.echoeyecodes.sinnerman.Activities.VideoListActivity
import com.echoeyecodes.sinnerman.Adapters.ExploreAdapter
import com.echoeyecodes.sinnerman.Interface.ExploreFragmentContext
import com.echoeyecodes.sinnerman.MainActivity
import com.echoeyecodes.sinnerman.Models.ExploreResponseBody
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.RootBottomFragment
import com.echoeyecodes.sinnerman.Utils.CustomScrollListener
import com.echoeyecodes.sinnerman.viewmodel.ExploreViewModel
import com.echoeyecodes.sinnerman.viewmodel.NetworkState


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

                //necessary call to force notification update
                // due to the diffutil.callback comparison when
                //the state changes from loading to error or vice-versa
                exploreAdapter.notifyItemChanged(exploreAdapter.itemCount - 1)
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

    override fun navigateToVideoListActivity(key:String, title: String) {
        val intent =Intent(context, VideoListActivity::class.java)
        intent.putExtra("title", title)
        intent.putExtra("key", key)
        startActivity(intent)
    }

    override fun onRefresh() {
        exploreViewModel.refresh()
    }

}