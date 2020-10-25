package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.*;

import androidx.recyclerview.widget.ListAdapter;
import com.bumptech.glide.Glide;
import com.example.myapplication.BottomNavigationFragments.HomeFragment;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.Models.VideoResponseBody;
import com.example.myapplication.Paging.CommonListPagingListeners;
import com.example.myapplication.Paging.CommonListPagingViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.viewmodel.NetworkState;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.io.Serializable;

public class HomeFragmentRecyclerViewAdapter extends ListAdapter<VideoResponseBody, RecyclerView.ViewHolder> implements CommonListPagingListeners {
    private final MainActivityContext mainActivityContext;
    private final Context context;
    private HomeFragment homeFragment;
    private NetworkState networkState;

    public HomeFragmentRecyclerViewAdapter(DiffUtil.ItemCallback<VideoResponseBody> itemCallback, Context context, HomeFragment fragment) {
        super(itemCallback);
        this.mainActivityContext = (MainActivityContext) context;
        this.homeFragment = fragment;
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_item, parent, false);
        return new HomeFragmentRecyclerViewItemViewHolder(view, this);
    }

    public void onNetworkStateChanged(NetworkState netState) {
        this.networkState = netState;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        VideoResponseBody videoResponseBody = getItem(position);

        HomeFragmentRecyclerViewItemViewHolder viewHolder = (HomeFragmentRecyclerViewItemViewHolder) holder;

        if(videoResponseBody == null){
            viewHolder.linearLayout.setVisibility(View.GONE);
            viewHolder.handleNetworkStateChanged(networkState);
            return;
        }
            viewHolder.linearLayout.setVisibility(View.VISIBLE);
            viewHolder.getLoading_container().setVisibility(View.GONE);
            Glide.with(context).load(Uri.parse(videoResponseBody.getVideo().getThumbnail())).into(viewHolder.imageView);
            Glide.with(context).load(Uri.parse(videoResponseBody.getUser().getProfile_url())).into(viewHolder.author_image);

            viewHolder.title.setText(videoResponseBody.getVideo().getTitle());
            viewHolder.author.setText(videoResponseBody.getUser().getUsername().concat(" ~ ").concat(String.valueOf(videoResponseBody.getVideo().getViews()).concat(" views")));

            viewHolder.linearLayout.setOnClickListener(v -> {
//                        mainActivityContext.navigateToVideos(videoResponseBody.getVideo().getId());
                homeFragment.doSomething(videoResponseBody);
            });
    }

    @Override
    public void retry() {
        homeFragment.retry();
    }

    public static class HomeFragmentRecyclerViewItemViewHolder extends CommonListPagingViewHolder {
        private final ImageView imageView;
        private final CircleImageView author_image;
        private final TextView title;
        private final TextView author;
        private final CardView cardView;
        private final LinearLayout linearLayout;
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

        public HomeFragmentRecyclerViewItemViewHolder(@NonNull View itemView, HomeFragmentRecyclerViewAdapter adapter) {
            super(itemView, adapter);

            linearLayout = itemView.findViewById(R.id.video_item);
            imageView = itemView.findViewById(R.id.video_thumbnail);
            cardView = itemView.findViewById(R.id.video_card_frame);
            title = itemView.findViewById(R.id.video_title);
            author_image = itemView.findViewById(R.id.author_image);
            author = itemView.findViewById(R.id.video_author);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cardView.getLayoutParams();
            layoutParams.height = (int) (displayMetrics.heightPixels / 3.5);
            cardView.setLayoutParams(layoutParams);
        }
    }

}