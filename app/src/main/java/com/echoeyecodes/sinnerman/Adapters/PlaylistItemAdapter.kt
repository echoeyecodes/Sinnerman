package com.echoeyecodes.sinnerman.Adapters;

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.echoeyecodes.sinnerman.Fragments.MoreOptionsFragment
import com.echoeyecodes.sinnerman.Fragments.MoreOptionsFragment.Companion.newInstance
import com.echoeyecodes.sinnerman.Models.VideoResponseBody
import com.echoeyecodes.sinnerman.R
import com.echoeyecodes.sinnerman.Utils.DurationConverter
import com.echoeyecodes.sinnerman.Utils.ImageColorDrawable
import com.echoeyecodes.sinnerman.Utils.TimestampConverter
import de.hdodenhof.circleimageview.CircleImageView
import java.io.Serializable

class PlaylistItemAdapter(itemCallback: DiffUtil.ItemCallback<VideoResponseBody>, private val context: Context, private val navigate: (String) -> Unit) : ListAdapter<VideoResponseBody, PlaylistItemAdapter.PlaylistItemViewHolder>(itemCallback), Serializable {

    private var fragmentManager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
    private lateinit var moreOptionsFragment: MoreOptionsFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistItemAdapter.PlaylistItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_feed_item, parent, false)
        return PlaylistItemViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PlaylistItemAdapter.PlaylistItemViewHolder, position: Int) {
        val videoResponseBody = getItem(position);

        if (videoResponseBody != null) {
            holder.bind(videoResponseBody)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return R.layout.layout_feed_item
        }
        return R.layout.layout_feed_item_secondary
    }


    inner class PlaylistItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.video_thumbnail)
        val linearLayout: LinearLayout = itemView.findViewById(R.id.video_item)
        val author_image: CircleImageView = itemView.findViewById(R.id.author_image)
        val frameLayout:FrameLayout = itemView.findViewById(R.id.feed_item_container)
        val title: TextView = itemView.findViewById(R.id.video_title)
        val options_btn: ImageView = itemView.findViewById(R.id.video_option_btn);
        val author: TextView = itemView.findViewById(R.id.video_author)
        val duration: TextView = itemView.findViewById(R.id.video_duration)

        init {
            val displayMetrics = Resources.getSystem().displayMetrics
            val cardView: CardView = itemView.findViewById(R.id.video_card_frame)

            val containerParams = frameLayout.layoutParams as RecyclerView.LayoutParams
            val cardViewLayoutParams = cardView.layoutParams as LinearLayout.LayoutParams

            containerParams.width = (displayMetrics.widthPixels * 0.8).toInt()
            cardViewLayoutParams.height = (displayMetrics.heightPixels / 3.5).toInt()

            cardView.layoutParams = cardViewLayoutParams
            frameLayout.layoutParams = containerParams
        }

        fun bind(videoResponseBody: VideoResponseBody) {
            Glide.with(context).load(Uri.parse(videoResponseBody.video.thumbnail)).placeholder(ImageColorDrawable.Companion.getInstance()).into(imageView);
            Glide.with(context).load(Uri.parse(videoResponseBody.user.profile_url)).placeholder(ImageColorDrawable.Companion.getInstance()).into(author_image);
            title.text = videoResponseBody.video.title
            author.text = videoResponseBody.user.fullname
            duration.text = DurationConverter.getInstance().convertToDuration(videoResponseBody.video.duration)
            val timestamp = TimestampConverter.getInstance().convertToTimeDifference(videoResponseBody.video.createdAt)
            author.text = videoResponseBody.user.username + " \u2022 " + videoResponseBody.video.views.toString() + " views" + " \u2022 " + timestamp

            linearLayout.setOnClickListener { navigate(videoResponseBody.video.id) }

            options_btn.setOnClickListener {
                moreOptionsFragment = newInstance(getItem(layoutPosition))
                moreOptionsFragment.show(fragmentManager, "more_options_fragment")
            }
        }
    }
}