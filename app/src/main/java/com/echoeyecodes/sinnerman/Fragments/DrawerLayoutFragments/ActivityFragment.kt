package com.echoeyecodes.sinnerman.Activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.echoeyecodes.sinnerman.Adapters.HomeFragmentRecyclerViewAdapter
import com.echoeyecodes.sinnerman.DrawerFragments
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.Paging.CommonListPagingListeners
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.Utils.*
import com.echoeyecodes.sinnerman.viewmodel.ActivityFragmentViewModel
import com.echoeyecodes.sinnerman.viewmodel.VideoListViewModel
import kotlin.collections.ArrayList

class ActivityFragment(val activityContext:String) : DrawerFragments(), SwipeRefreshLayout.OnRefreshListener, CommonListPagingListeners{
    private lateinit var toolbar: LinearLayout
    private lateinit var relativeLayout: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeFragmentRecyclerViewAdapter
    private lateinit var viewModel: ActivityFragmentViewModel
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var linearLayoutManager: LinearLayoutManager


    init {
        TAG="ACTIVITY_FRAGMENT_".plus(activityContext)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_video_list, container, false)
    }

    override fun onResume() {
        super.onResume()
        mainActivityContext.onDrawerFragmentActive(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        relativeLayout = view.findViewById(R.id.video_list_activity_root)
        toolbar = view.findViewById(R.id.video_list_toolbar)
        toolbar.visibility = View.GONE

        viewModel = ViewModelProvider(this).get(ActivityFragmentViewModel::class.java)
        viewModel.context = activityContext

        swipeRefreshLayout = view.findViewById(R.id.home_swipe_refresh)
        recyclerView = view.findViewById(R.id.video_list_recycler_view)

        swipeRefreshLayout.setOnRefreshListener(this)

        adapter = HomeFragmentRecyclerViewAdapter(SealedClassDiffUtil(), requireContext(), this)
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addItemDecoration(CustomItemDecoration(IntegerToDp.intToDp(15), IntegerToDp.intToDp(15)))

        recyclerView.adapter = adapter

        viewModel.videos.observe(viewLifecycleOwner, Observer<List<VideoResponseBody>> { videos ->
            val items = videos.map { Result.Success(it) }
            adapter.submitList(items)
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

    override fun onRefresh() {
        viewModel.refresh()
    }

    override fun retry() {
        viewModel.retry()
    }

    override fun onItemsChanged() {

    }

    private fun fetchMore(){
        viewModel.fetchMore(Result.Loading);
    }

}