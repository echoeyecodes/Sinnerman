package com.echoeyecodes.sinnerman.Fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.sinnerman.Interface.MainActivityContext
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MoreOptionsFragment(private val video:VideoResponseBody) : BottomSheetDialogFragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var mainActivityContext: MainActivityContext

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_more_options_list_dialog, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivityContext = context as MainActivityContext
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = view.findViewById(R.id.video_options_recycler_view)
        val layoutManager = LinearLayoutManager(context)
        val adapter = ItemAdapter();

        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    private inner class ViewHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.fragment_more_options_list_dialog_item, parent, false)) {

        val text: TextView = itemView.findViewById(R.id.text)
    }

    private inner class ItemAdapter() : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(parent.context), parent)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            when (position) {
                0 -> {
                    holder.text.text = "Copy Link"
                    holder.text.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_link, null), null,null,null)
                }
                1 -> {
                    holder.text.text = "Share Link"
                    holder.text.setCompoundDrawablesWithIntrinsicBounds(ResourcesCompat.getDrawable(requireContext().resources, R.drawable.ic_share, null), null,null,null)
                }
            }

            holder.text.setOnClickListener {
                mainActivityContext.onOptionSelected(video, position)
                dismiss()
            }
        }

        override fun getItemCount(): Int {
            return 2
        }
    }

    companion object {
        fun newInstance(video: VideoResponseBody): MoreOptionsFragment = MoreOptionsFragment(video)
    }
}