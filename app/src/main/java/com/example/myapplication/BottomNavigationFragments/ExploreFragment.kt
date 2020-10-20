package com.example.myapplication.BottomNavigationFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Activities.VideoListActivity
import com.example.myapplication.Adapters.ExploreAdapter
import com.example.myapplication.Interface.ExploreFragmentContext
import com.example.myapplication.MainActivity
import com.example.myapplication.Models.ExploreResponseBody
import com.example.myapplication.R
import com.example.myapplication.RootBottomFragment
import com.example.myapplication.viewmodel.ExploreViewModel


class ExploreFragment : RootBottomFragment(), ExploreFragmentContext {
    private var recyclerView : RecyclerView? = null
    private var linearLayoutManager : LinearLayoutManager? = null
    private var exploreViewModel: ExploreViewModel? = null;
    private var exploreAdapter : ExploreAdapter? = null

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
        exploreViewModel = ViewModelProvider(requireActivity()).get(ExploreViewModel::class.java)


        linearLayoutManager = LinearLayoutManager(context)

        recyclerView!!.layoutManager = linearLayoutManager
        exploreAdapter = ExploreAdapter(context!!, this::navigateToVideoListActivity, mainActivity::navigateToVideos, ExploreResponseItemCallback())
        recyclerView!!.adapter = exploreAdapter

        exploreViewModel!!.videosObserver.observe(viewLifecycleOwner, Observer<PagingData<ExploreResponseBody>> { value ->
            exploreAdapter!!.submitData(lifecycle, value)
        })

    }

    inner class ExploreResponseItemCallback : DiffUtil.ItemCallback<ExploreResponseBody>() {

        override fun areItemsTheSame(oldItem: ExploreResponseBody, newItem: ExploreResponseBody): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExploreResponseBody, newItem: ExploreResponseBody): Boolean {
            return oldItem.name == newItem.name
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView?.adapter = null
    }

    override fun onResume() {
        super.onResume()
        mainActivityContext.setActiveBottomViewFragment(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        exploreAdapter?.resetRecyclerStates()
    }

    override fun navigateToVideoListActivity(title: String?) {
        val intent =Intent(context, VideoListActivity::class.java)
        intent.putExtra("title", title)
        startActivity(intent)
    }

}