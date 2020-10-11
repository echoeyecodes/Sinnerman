package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.*;

import androidx.recyclerview.widget.ListAdapter;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class HomeFragmentRecyclerViewAdapter extends ListAdapter<VideoModel, HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder> implements Serializable {
    private final Context context;
    private final MainActivityContext mainActivityContext;
    private HomeFragmentRecyclerViewItemViewHolder homeFragmentRecyclerViewItemViewHolder;

    public HomeFragmentRecyclerViewAdapter(DiffUtil.ItemCallback<VideoModel> itemCallback, Context context, MainActivityContext mainActivityContext) {
        super(itemCallback);
        this.context = context;
        this.mainActivityContext = mainActivityContext;
    }

    @NonNull
    @Override
    public HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_item, parent, false);
        homeFragmentRecyclerViewItemViewHolder = new HomeFragmentRecyclerViewItemViewHolder(view);
        return homeFragmentRecyclerViewItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder holder, int position) {
        Picasso.get().load(Uri.parse(getItem(position).getThumbnail())).into(holder.imageView);

        holder.linearLayout.setOnClickListener(v -> {
            mainActivityContext.navigateToVideos(getItem(position).getVideo_url());
        });
    }

    public HomeFragmentRecyclerViewItemViewHolder getHomeAdapterViewHolder(){
        return homeFragmentRecyclerViewItemViewHolder;
    }


    public static class HomeFragmentRecyclerViewItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private CardView cardView;
        private LinearLayout linearLayout;
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

        public HomeFragmentRecyclerViewItemViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.video_item);
            imageView = itemView.findViewById(R.id.video_thumbnail);
            cardView = itemView.findViewById(R.id.video_card_frame);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cardView.getLayoutParams();
            layoutParams.height = (int) (displayMetrics.heightPixels / 3.5);
            cardView.setLayoutParams(layoutParams);
        }
    }
}