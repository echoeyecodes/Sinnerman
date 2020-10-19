package com.example.myapplication.BottomNavigationFragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Activities.VideoListActivity
import com.example.myapplication.Adapters.ExploreAdapter
import com.example.myapplication.Interface.ExploreFragmentContext
import com.example.myapplication.Models.VideoModel
import com.example.myapplication.R
import com.example.myapplication.RootBottomFragment
import com.example.myapplication.viewmodel.ExploreViewModel
import com.example.myapplication.viewmodel.MainActivityViewModel

import java.util.HashMap
import java.util.Map


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

        recyclerView = view.findViewById(R.id.explore_recycler_view)
        exploreViewModel = ViewModelProvider(requireActivity()).get(ExploreViewModel::class.java)


        val mock_data = HashMap<String, List<VideoModel>>()

        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        recyclerView!!.layoutManager = linearLayoutManager
        exploreAdapter = ExploreAdapter(mock_data, context, mainActivityContext!!, this)
        recyclerView!!.adapter = exploreAdapter

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