package com.example.myapplication.Adapters;

import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.cardview.widget.CardView;
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.*;
import androidx.recyclerview.widget.ListAdapter
import com.example.myapplication.Models.VideoResponseBody;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView

import java.io.Serializable;

 class ExploreItemAdapter(itemCallback: DiffUtil.ItemCallback<VideoResponseBody>, private val navigate: (String) -> Unit) : ListAdapter<VideoResponseBody, ExploreItemAdapter.ExploreItemViewHolder>(itemCallback), Serializable {


    override fun onCreateViewHolder( parent : ViewGroup, viewType : Int): ExploreItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_feed_item, parent, false);
        val displayMetrics = Resources.getSystem().displayMetrics;
        val linearLayout :LinearLayout = view.findViewById(R.id.video_item);
        val cardView : CardView = view.findViewById(R.id.video_card_frame);

        val layoutParams = linearLayout.layoutParams as RecyclerView.LayoutParams;
        val cardViewLayoutParams = cardView.layoutParams as LinearLayout.LayoutParams;
        layoutParams.width = (displayMetrics.widthPixels * 0.8).toInt()
        cardViewLayoutParams.height = (displayMetrics.heightPixels / 3.5).toInt();

        cardView.layoutParams = cardViewLayoutParams;
        linearLayout.layoutParams = layoutParams;
        return ExploreItemViewHolder(view);
    }

    override fun onBindViewHolder(holder : ExploreItemAdapter.ExploreItemViewHolder, position : Int) {
        val videoResponseBody = getItem(position);

        if(videoResponseBody != null) {
            Picasso.get().load(Uri.parse(videoResponseBody.video.thumbnail)).into(holder.imageView);
            Picasso.get().load(Uri.parse(videoResponseBody.user.profile_url)).into(holder.author_image);
            holder.title.text = videoResponseBody.video.title
            holder.author.text = videoResponseBody.user.fullname
            holder.linearLayout.setOnClickListener{ navigate(videoResponseBody.video.id) }
        }
    }

    inner class ExploreItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.video_thumbnail);
        val cardView : CardView = itemView.findViewById(R.id.video_card_frame);
        val linearLayout : LinearLayout = itemView.findViewById(R.id.video_item);
        val author_image:CircleImageView = itemView.findViewById(R.id.author_image)
        val title : TextView = itemView.findViewById(R.id.video_title);
        val author : TextView = itemView.findViewById(R.id.video_author);


    }
}