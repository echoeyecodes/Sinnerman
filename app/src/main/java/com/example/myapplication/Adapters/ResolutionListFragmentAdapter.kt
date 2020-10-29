package com.example.myapplication.Adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Fragments.ResolutionListFragment
import com.example.myapplication.Interface.VideoActivityListener
import com.example.myapplication.Models.ResolutionDimensions
import com.example.myapplication.R

class ResolutionListFragmentAdapter(itemCallback: DiffUtil.ItemCallback<ResolutionDimensions>, private val context:Context, private val selectedItemPosition: Int, private val videoActivityListener: VideoActivityListener) : ListAdapter<ResolutionDimensions, ResolutionListFragmentAdapter.ResolutionListFragmentViewHolder>(itemCallback){

    private var recyclerView: RecyclerView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResolutionListFragmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_resolution_list_list_dialog_item, parent, false)
        return ResolutionListFragmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: ResolutionListFragmentViewHolder, position: Int) {
        val resolution = getItem(position)

        if(position == 0){
            holder.textView.text = "Auto"
        }else{
            holder.textView.text = "${resolution.height}p"
        }
        
        holder.linearLayout.setOnClickListener { videoActivityListener.onResolutionSelected(position, resolution) }

        if(position == selectedItemPosition){
            holder.imageView.visibility = View.VISIBLE
        }else{
            holder.imageView.visibility = View.GONE
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }


    inner class ResolutionListFragmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val linearLayout : LinearLayout = itemView.findViewById(R.id.resolution_item)
        val textView: TextView = itemView.findViewById(R.id.resolution_list_text);
        val imageView: ImageView = itemView.findViewById(R.id.resolution_selected_icon);
    }

}