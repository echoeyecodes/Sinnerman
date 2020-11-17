package com.echoeyecodes.sinnerman.Fragments.BottomNavigationFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.echoeyecodes.sinnerman.Activities.VideoListActivity
import com.echoeyecodes.sinnerman.Adapters.PlaylistAdapter
import com.echoeyecodes.sinnerman.Interface.ExploreFragmentContext
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.Interface.PrimaryFragmentContext
import com.echoeyecodes.sinnerman.MainActivity
import com.echoeyecodes.sinnerman.Models.ExploreResponseBody
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.RootBottomFragment
import com.echoeyecodes.sinnerman.Utils.CustomScrollListener
import com.echoeyecodes.sinnerman.Utils.Result
import com.echoeyecodes.sinnerman.Utils.SealedClassDiffUtil
import com.echoeyecodes.sinnerman.viewmodel.BottomFragmentViewModel.ExploreViewModel


class ExploreFragment(private val primaryFragmentContext: PrimaryFragmentContext) : RootBottomFragment(), ExploreFragmentContext, SwipeRefreshLayout.OnRefreshListener{
    private lateinit var recyclerView : RecyclerView
    private lateinit var linearLayoutManager : LinearLayoutManager
    private lateinit var exploreViewModel: ExploreViewModel
    private lateinit var exploreAdapter : PlaylistAdapter
    private lateinit var swipeRefreshLayout : SwipeRefreshLayout

    init{
        TAG ="EXPLORE_FRAGMENT"
    }

    companion object{
        fun newInstance(primaryFragmentContext: PrimaryFragmentContext, mainActivityContext: MainActivityContext): ExploreFragment = ExploreFragment(primaryFragmentContext)
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
        exploreViewModel = ViewModelProvider(this).get(ExploreViewModel::class.java)


        linearLayoutManager = LinearLayoutManager(context)

        recyclerView.layoutManager = linearLayoutManager
        exploreAdapter = PlaylistAdapter(
                exploreFragmentContext = this,
                context = requireContext(),
                navigateToMore =  this::navigateToVideoListActivity,
                navigateToVideo = mainActivity::navigateToVideos,
                itemCallback = SealedClassDiffUtil())

        exploreAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        recyclerView.adapter = exploreAdapter

        exploreViewModel.categories.observe(viewLifecycleOwner, Observer<List<ExploreResponseBody>> { value ->
            val currentState = exploreViewModel.networkStatus.value

            if(currentState == Result.Idle){
                val items = value.map { Result.Success(it) }
                exploreAdapter.submitList(items)
            }
        })

        exploreViewModel.networkStatus.observe(viewLifecycleOwner, Observer<Result<ExploreResponseBody>> { state ->
            when (state) {
                is Result.Loading -> {
                    val originalItems = ArrayList(exploreViewModel.state.map { Result.Success(it) }) + Result.Loading
                    exploreAdapter.submitList(originalItems)
                }
                is Result.Error -> {
                    val originalItems = ArrayList(exploreViewModel.state.map { Result.Success(it) }) + Result.Error
                    exploreAdapter.submitList(originalItems)
                }
                is Result.Refreshing -> swipeRefreshLayout.isRefreshing = true
                else -> { }
            }

            swipeRefreshLayout.isRefreshing = state == Result.Refreshing
        })

        recyclerView.addOnScrollListener(CustomScrollListener(fetchMore = this::fetchMore))
    }


    private fun fetchMore(){
        exploreViewModel.fetchMore(Result.Loading);
    }

    override fun onResume() {
        super.onResume()
        primaryFragmentContext.setActiveBottomViewFragment(1)
    }

    override fun retry(){
        exploreViewModel.retry()
    }


    override fun onItemsChanged() {

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