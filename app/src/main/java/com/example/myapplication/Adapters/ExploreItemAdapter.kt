package com.example.myapplication.Adapters;

import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Models.VideoResponseBody
import com.example.myapplication.R
import com.example.myapplication.Utils.DurationConverter
import com.example.myapplication.Utils.TimestampConverter
import de.hdodenhof.circleimageview.CircleImageView
import java.io.Serializable

class ExploreItemAdapter(itemCallback: DiffUtil.ItemCallback<VideoResponseBody>, private val context: Context, private val navigate: (String) -> Unit) : ListAdapter<VideoResponseBody, ExploreItemAdapter.CustomViewHolder>(itemCallback), Serializable {

     companion object{
         const val PRIMARY = 0
         const val SECONDARY = 1
     }

    override fun onCreateViewHolder( parent : ViewGroup, viewType : Int): ExploreItemAdapter.CustomViewHolder {
        return if(viewType == PRIMARY){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_feed_item, parent, false)
            ExplorePrimaryItemViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_feed_item_secondary, parent, false)
            ExploreSecondaryItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder : ExploreItemAdapter.CustomViewHolder, position : Int) {
        val videoResponseBody = getItem(position);

        if(videoResponseBody != null) {
            Glide.with(context).load(Uri.parse(videoResponseBody.video.thumbnail)).into(holder.imageView);
            Glide.with(context).load(Uri.parse(videoResponseBody.user.profile_url)).into(holder.author_image);
            holder.title.text = videoResponseBody.video.title
            holder.author.text = videoResponseBody.user.fullname

            holder.duration.text = DurationConverter.getInstance().convertToDuration(videoResponseBody.video.duration)
            val timestamp = TimestampConverter.getInstance().convertToTimeDifference(videoResponseBody.video.createdAt)
            holder.author.text = videoResponseBody.user.username + " \u2022 " + videoResponseBody.video.views.toString() + " views" + " \u2022 " + timestamp
            holder.linearLayout.setOnClickListener{ navigate(videoResponseBody.video.id) }
        }
    }

     override fun getItemViewType(position: Int): Int {
        if(position == 0){
            return PRIMARY
        }
         return SECONDARY
     }

     open inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
         val imageView : ImageView = itemView.findViewById(R.id.video_thumbnail)
         val linearLayout : LinearLayout = itemView.findViewById(R.id.video_item)
         val author_image:CircleImageView = itemView.findViewById(R.id.author_image)
         val title : TextView = itemView.findViewById(R.id.video_title)
         val author : TextView = itemView.findViewById(R.id.video_author)
         val duration : TextView = itemView.findViewById(R.id.video_duration)
     }

    inner class ExplorePrimaryItemViewHolder(itemView: View) : CustomViewHolder(itemView) {

        init {
            val displayMetrics = Resources.getSystem().displayMetrics
            val cardView : CardView = itemView.findViewById(R.id.video_card_frame)

            val cardViewLayoutParams = cardView.layoutParams as LinearLayout.LayoutParams
            cardViewLayoutParams.height = (displayMetrics.heightPixels / 3.5).toInt()

            cardView.layoutParams = cardViewLayoutParams
        }
    }

     inner class ExploreSecondaryItemViewHolder(itemView: View) : CustomViewHolder(itemView) {
         init {
             val displayMetrics = Resources.getSystem().displayMetrics
             val cardView : CardView = itemView.findViewById(R.id.video_card_frame)

             val cardViewLayoutParams = cardView.layoutParams as LinearLayout.LayoutParams
             cardViewLayoutParams.width = (displayMetrics.widthPixels /2).toInt()
             cardViewLayoutParams.height = (displayMetrics.heightPixels / 6).toInt()

             cardView.layoutParams = cardViewLayoutParams
         }
     }
}