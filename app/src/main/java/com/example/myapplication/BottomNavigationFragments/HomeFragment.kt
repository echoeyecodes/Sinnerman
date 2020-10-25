package com.example.myapplication.BottomNavigationFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter
import com.example.myapplication.Adapters.VideoLoadStateAdapter
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.R
import com.example.myapplication.RootBottomFragment
import com.example.myapplication.Utils.*
import com.example.myapplication.viewmodel.BottomFragmentViewModel.HomeFragmentViewModel
import com.example.myapplication.viewmodel.NetworkState
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : RootBottomFragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var loading_error_container: LinearLayout
    private lateinit var loading_screen: RelativeLayout
    private lateinit var progress_bar: ProgressBar
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(HomeFragmentViewModel::class.java)
        swipeRefreshLayout = view.findViewById(R.id.home_swipe_refresh)
        loading_screen = view.findViewById(R.id.home_fragment_loading_layout)
        loading_error_container = view.findViewById(R.id.loading_error_container)
        progress_bar = view.findViewById(R.id.loading_progress_bar);
        recyclerView = view.findViewById(R.id.fragment_home_recycler_view)

        swipeRefreshLayout.setOnRefreshListener(this)

        val videosItemCallback = VideosItemCallback.newInstance()

        adapter = HomeFragmentRecyclerViewAdapter(videosItemCallback, context, this)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = linearLayoutManager

        recyclerView.addItemDecoration(CustomItemDecoration(IntegerToDp.intToDp(15), IntegerToDp.intToDp(15)))

        recyclerView.adapter = adapter


        viewModel.networkStatus.observe(viewLifecycleOwner, Observer<NetworkState> {state ->
                when(state){
                    NetworkState.REFRESHING -> swipeRefreshLayout.isRefreshing = true
                    NetworkState.LOADING -> {
                        loading_screen.visibility = View.VISIBLE
                        progress_bar.visibility = View.VISIBLE
                    }
                    NetworkState.ERROR -> {
                        progress_bar.visibility = View.GONE
                        loading_screen.visibility = View.VISIBLE
                        loading_error_container.visibility = View.VISIBLE
                    }
                    else -> {
                        loading_screen.visibility = View.GONE
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
        })


        viewModel.roomDao.getVideos().observe(viewLifecycleOwner, Observer<List<VideoResponseBody>> { videos ->
            adapter?.submitList(videos)
        })



        recyclerView.addOnScrollListener(CustomScrollListener(fetchMore = viewModel::fetchMore))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
    }

    override fun onResume() {
        super.onResume()
        mainActivityContext.setActiveBottomViewFragment(0)
    }

    fun doSomething(item: VideoResponseBody){
        viewModel.insertUpdateToVideoList(item)
    }

    override fun onRefresh() {
        viewModel.refresh()
    }

    inner class ChipsAdapter(private val items: Array<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_home_chips_item, parent, false)
            return ChipsViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as ChipsViewHolder).chip.text = items[position]
        }

        override fun getItemCount(): Int {
            return items.size
        }

        inner class ChipsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val chip: Chip = itemView.findViewById(R.id.home_chips_item)
        }
    }
}
