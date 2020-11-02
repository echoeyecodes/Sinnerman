package com.echoeyecodes.sinnerman.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.*;

import androidx.recyclerview.widget.ListAdapter;
import com.bumptech.glide.Glide;
import com.echoeyecodes.sinnerman.BottomNavigationFragments.HomeFragment;
import com.echoeyecodes.sinnerman.Fragments.MoreOptionsFragment;
import com.echoeyecodes.sinnerman.Interface.HomeFragmentListener;
import com.echoeyecodes.sinnerman.Interface.MainActivityContext;
import com.echoeyecodes.sinnerman.Interface.PagingListener;
import com.echoeyecodes.sinnerman.Models.VideoResponseBody;
import com.echoeyecodes.sinnerman.Paging.CommonListPagingListeners;
import com.echoeyecodes.sinnerman.Paging.CommonListPagingViewHolder;
import com.echoeyecodes.sinnerman.R;
import com.echoeyecodes.sinnerman.Utils.DurationConverter;
import com.echoeyecodes.sinnerman.Utils.TimestampConverter;
import com.echoeyecodes.sinnerman.viewmodel.NetworkState;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragmentRecyclerViewAdapter extends ListAdapter<VideoResponseBody, RecyclerView.ViewHolder> {
    private final MainActivityContext mainActivityContext;
    private final Context context;
    private final HomeFragmentListener homeFragmentListener;
    private FragmentManager fragmentManager;
    private MoreOptionsFragment moreOptionsFragment;

    public HomeFragmentRecyclerViewAdapter(DiffUtil.ItemCallback<VideoResponseBody> itemCallback, Context context, HomeFragmentListener homeFragmentListener) {
        super(itemCallback);
        this.mainActivityContext = (MainActivityContext) context;
        this.homeFragmentListener = homeFragmentListener;
        this.context = context.getApplicationContext();

        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_item, parent, false);
        return new HomeFragmentRecyclerViewItemViewHolder(view, homeFragmentListener);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        VideoResponseBody videoResponseBody = getItem(position);

        HomeFragmentRecyclerViewItemViewHolder viewHolder = (HomeFragmentRecyclerViewItemViewHolder) holder;

        if(videoResponseBody == null){
            viewHolder.linearLayout.setVisibility(View.GONE);
            viewHolder.handleNetworkStateChanged(homeFragmentListener.onNetworkStateChanged());
            return;
        }
            viewHolder.linearLayout.setVisibility(View.VISIBLE);
            viewHolder.getLoading_container().setVisibility(View.GONE);
            Glide.with(context).load(Uri.parse(videoResponseBody.getVideo().getThumbnail())).into(viewHolder.imageView);
            Glide.with(context).load(Uri.parse(videoResponseBody.getUser().getProfile_url())).into(viewHolder.author_image);

            viewHolder.title.setText(videoResponseBody.getVideo().getTitle());
            viewHolder.duration.setText(DurationConverter.Companion.getInstance().convertToDuration(videoResponseBody.getVideo().getDuration()));
            String timestamp = TimestampConverter.Companion.getInstance().convertToTimeDifference(videoResponseBody.getVideo().getCreatedAt());
            viewHolder.author.setText(videoResponseBody.getUser().getUsername().concat(" \u2022 ").concat(String.valueOf(videoResponseBody.getVideo().getViews()).concat(" views")).concat(" \u2022 ").concat(timestamp));

            viewHolder.linearLayout.setOnClickListener(v -> {
                mainActivityContext.navigateToVideos(videoResponseBody.getVideo().getId());
            });

            viewHolder.options_btn.setOnClickListener(v ->{
                moreOptionsFragment = MoreOptionsFragment.Companion.newInstance(getItem(position));
                moreOptionsFragment.show(fragmentManager, "more_options_fragment");
            });
    }


    public static class HomeFragmentRecyclerViewItemViewHolder extends CommonListPagingViewHolder {
        private final ImageView imageView;
        private final CircleImageView author_image;
        private final TextView title;
        private final TextView author;
        private final CardView cardView;
        private final ImageView options_btn;
        private final LinearLayout linearLayout;
        private final TextView duration;
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

        public HomeFragmentRecyclerViewItemViewHolder(@NonNull View itemView, HomeFragmentListener homeFragmentListener) {
            super(itemView, homeFragmentListener);

            linearLayout = itemView.findViewById(R.id.video_item);
            imageView = itemView.findViewById(R.id.video_thumbnail);
            cardView = itemView.findViewById(R.id.video_card_frame);
            title = itemView.findViewById(R.id.video_title);
            options_btn = itemView.findViewById(R.id.video_option_btn);
            author_image = itemView.findViewById(R.id.author_image);
            author = itemView.findViewById(R.id.video_author);
            duration = itemView.findViewById(R.id.video_duration);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cardView.getLayoutParams();
            layoutParams.height = (int) (displayMetrics.heightPixels / 3.5);
            cardView.setLayoutParams(layoutParams);
        }
    }

}