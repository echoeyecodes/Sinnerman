package com.example.myapplication.BottomNavigationFragments

import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.Toast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter
import com.example.myapplication.Adapters.VideoLoadStateAdapter
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.R
import com.example.myapplication.RootBottomFragment
import com.example.myapplication.Utils.CustomItemDecoration
import com.example.myapplication.Utils.IntegerToDp
import com.example.myapplication.Utils.VideosItemCallback
import com.example.myapplication.viewmodel.BottomFragmentViewModel.HomeFragmentViewModel
import com.google.android.material.chip.Chip


class HomeFragment : RootBottomFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var recyclerView: RecyclerView? = null
    private var chipsRecyclerView: RecyclerView? = null
    private var loading_screen: RelativeLayout? = null
    private var adapter: HomeFragmentRecyclerViewAdapter? = null
    private var viewModel: HomeFragmentViewModel? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    var linearLayoutManager: LinearLayoutManager? = null
    private var chipsLayoutManager: LinearLayoutManager? = null
    private val items = arrayOf("Recent", "Most Liked", "Most Comment", "Top Views")


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
        recyclerView = view.findViewById(R.id.fragment_home_recycler_view)
        chipsRecyclerView = view.findViewById(R.id.video_filters)

        swipeRefreshLayout!!.setOnRefreshListener(this)

        val videosItemCallback = VideosItemCallback.newInstance()

        adapter = HomeFragmentRecyclerViewAdapter(videosItemCallback, context, mainActivityContext)
        val chipsAdapter = ChipsAdapter(items)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        chipsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        chipsRecyclerView?.layoutManager = chipsLayoutManager
        recyclerView?.layoutManager = linearLayoutManager

        chipsRecyclerView?.addItemDecoration(CustomItemDecoration(0, IntegerToDp.intToDp(5)))
        recyclerView?.addItemDecoration(CustomItemDecoration(IntegerToDp.intToDp(15), IntegerToDp.intToDp(15)))
        chipsRecyclerView?.adapter = chipsAdapter


        recyclerView?.adapter = adapter!!.withLoadStateFooter(footer = VideoLoadStateAdapter())
        chipsAdapter.notifyDataSetChanged()

        viewModel!!.videosObserver?.observe(viewLifecycleOwner, Observer<PagingData<VideoResponseBody>> { videos ->
            adapter!!.submitData(lifecycle, videos)
            swipeRefreshLayout!!.isRefreshing = false
        })

        adapter!!.addLoadStateListener { state ->
            when (state.append) {
                is LoadState.Loading -> {
                    loading_screen!!.visibility = View.VISIBLE
                }
                else -> {
                    loading_screen!!.visibility = View.GONE
                }
            }

            when (state.refresh) {
                is LoadState.Loading -> {
                    swipeRefreshLayout!!.isRefreshing = true
                }
                else -> {
                    swipeRefreshLayout!!.isRefreshing = false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mainActivityContext.setActiveBottomViewFragment(0)
    }

    override fun onRefresh() {
        adapter?.refresh()
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
