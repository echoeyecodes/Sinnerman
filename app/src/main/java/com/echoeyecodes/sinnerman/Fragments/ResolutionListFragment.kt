package com.echoeyecodes.sinnerman.Fragments

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import com.echoeyecodes.sinnerman.Adapters.ResolutionListFragmentAdapter
import com.echoeyecodes.sinnerman.Interface.VideoActivityListener
import com.echoeyecodes.sinnerman.Models.ResolutionDimensions
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.viewmodel.VideoActivityViewModel

/**
 *
 * A fragment that shows a list of items as a modal bottom sheet.
 *
 * You can show this modal bottom sheet from your activity like this:
 * <pre>
 *    ResolutionListFragment.newInstance(30).show(supportFragmentManager, "dialog")
 * </pre>
 */
class ResolutionListFragment(private val data: List<ResolutionDimensions>) : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView;
    private lateinit var adapter: ResolutionListFragmentAdapter
    private lateinit var videoActivityListener: VideoActivityListener
    private lateinit var videoActivityViewModel: VideoActivityViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resolution_list_list_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.resolution_list_recycler_view);

        videoActivityViewModel = ViewModelProvider(requireActivity()).get(VideoActivityViewModel::class.java)

        adapter = ResolutionListFragmentAdapter(ResolutionItemCallback(), requireContext(), videoActivityViewModel.selectedItemPosition, videoActivityListener);

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = adapter

        adapter.submitList(data)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context !is VideoActivityListener){
            throw Exception("Must implement Video Activity Listener")
        }
        videoActivityListener = context
    }

    companion object {

        fun getInstance(data: List<ResolutionDimensions>): ResolutionListFragment =
                ResolutionListFragment(data)

    }

    inner class ResolutionItemCallback : DiffUtil.ItemCallback<ResolutionDimensions>() {

        override fun areItemsTheSame(oldItem: ResolutionDimensions, newItem: ResolutionDimensions): Boolean {
            return oldItem == newItem
        }


        override fun areContentsTheSame(oldItem: ResolutionDimensions, newItem: ResolutionDimensions): Boolean {
            return oldItem == newItem
        }

    }
}