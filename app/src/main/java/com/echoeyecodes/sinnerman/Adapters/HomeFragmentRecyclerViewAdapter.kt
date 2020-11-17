package com.echoeyecodes.sinnerman.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.echoeyecodes.sinnerman.Fragments.MoreOptionsFragment;
import com.echoeyecodes.sinnerman.Interface.MainActivityContext;
import com.echoeyecodes.sinnerman.Models.VideoResponseBody;
import com.echoeyecodes.sinnerman.Paging.AdsViewHolder
import com.echoeyecodes.sinnerman.Paging.CommonListPagingListeners
import com.echoeyecodes.sinnerman.Paging.CommonListPagingViewHolder
import com.echoeyecodes.sinnerman.R;
import com.echoeyecodes.sinnerman.Utils.*
import de.hdodenhof.circleimageview.CircleImageView;

class HomeFragmentRecyclerViewAdapter(itemCallback: DiffUtil.ItemCallback<Result<VideoResponseBody>>, private val context: Context, private val homeFragmentListener: CommonListPagingListeners) : PagerAdapter<VideoResponseBody>(itemCallback) {
    private val fragmentManager : FragmentManager  = (context as AppCompatActivity).supportFragmentManager
    private lateinit var moreOptionsFragment:MoreOptionsFragment
    private val mainActivityContext = context as MainActivityContext

    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    override fun onCurrentListChanged(previousList: MutableList<Result<VideoResponseBody>>, currentList: MutableList<Result<VideoResponseBody>>) {
        super.onCurrentListChanged(previousList, currentList)
        homeFragmentListener.onItemsChanged()
    }

    override fun onCreateViewHolder( parent : ViewGroup, viewType : Int) : RecyclerView.ViewHolder{
        if(viewType == LOADING_LAYOUT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.paging_loading_layout, parent, false)
            return CommonListPagingViewHolder(view, homeFragmentListener)
        }
        if(viewType == ADS_LAYOUT){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_ad_banner, parent, false)
            return AdsViewHolder(view)
        }
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_feed_item, parent, false);
        return HomeFragmentRecyclerViewItemViewHolder(view);
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is Result.Success -> {
                val videoResponseBody = (getItem(position) as Result.Success<VideoResponseBody>).data
                if(videoResponseBody.video.duration == "ad"){
                    return ADS_LAYOUT
                }
                return NORMAL_LAYOUT
            }
            else -> super.getItemViewType(position)
        }
    }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            when(getItem(position)){
                is Result.Success<*> ->{
                    val videoResponseBody = (getItem(position) as Result.Success<VideoResponseBody>).data
                    if(holder !is AdsViewHolder){
                        val viewHolder = holder as HomeFragmentRecyclerViewItemViewHolder
                        viewHolder.linearLayout.visibility = View.VISIBLE;
                        Glide.with(context).load(Uri.parse(videoResponseBody.video.thumbnail)).placeholder(ImageColorDrawable.getInstance()).into(viewHolder.imageView);
                        Glide.with(context).load(Uri.parse(videoResponseBody.user.profile_url)).placeholder(ImageColorDrawable.getInstance()).into(viewHolder.author_image);

                        viewHolder.title.text = videoResponseBody.video.title;
                        viewHolder.duration.text = DurationConverter.Companion.getInstance().convertToDuration(videoResponseBody.video.duration);
                        val timestamp = TimestampConverter.getInstance ().convertToTimeDifference(videoResponseBody.video.createdAt);
                        viewHolder.author.text = videoResponseBody.user.username.plus(" \u2022 ").plus(videoResponseBody.video.views.toString()).plus(" views").plus(" \u2022 ").plus(timestamp)

                        viewHolder.linearLayout.setOnClickListener {
                            mainActivityContext.navigateToVideos(videoResponseBody.video.id);
                        };

                        viewHolder.options_btn.setOnClickListener {
                            moreOptionsFragment = MoreOptionsFragment.newInstance(videoResponseBody);
                            moreOptionsFragment.show(fragmentManager, "more_options_fragment");
                        }
                    }else{
                        Glide.with(context).load(Uri.parse(videoResponseBody.video.thumbnail)).into(holder.imageView)
                        holder.linearLayout.setOnClickListener { mainActivityContext.openExternalLink(videoResponseBody.video.video_url) }
                    }
                }
                else -> {}
            }
        }


    inner class HomeFragmentRecyclerViewItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageView: ImageView = itemView.findViewById(R.id.video_thumbnail);
            val author_image: CircleImageView = itemView.findViewById(R.id.author_image);
            val title: TextView = itemView.findViewById(R.id.video_title);
            val author: TextView = itemView.findViewById(R.id.video_author);
            val cardView: CardView = itemView.findViewById(R.id.video_card_frame);
            val options_btn: ImageView = itemView.findViewById(R.id.video_option_btn);
            val linearLayout: LinearLayout = itemView.findViewById(R.id.video_item);
            val duration: TextView = itemView.findViewById(R.id.video_duration);
            val displayMetrics = Resources.getSystem().displayMetrics;


            init {
                val layoutParams = cardView.layoutParams as LinearLayout.LayoutParams
                layoutParams.height = (displayMetrics.heightPixels / 3.5).toInt();
                cardView.layoutParams = layoutParams;
            }


        }
    }
