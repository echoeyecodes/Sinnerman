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
    private transient final Context context;
    private transient final MainActivityContext mainActivityContext;

    public HomeFragmentRecyclerViewAdapter(DiffUtil.ItemCallback<VideoModel> itemCallback, Context context, MainActivityContext mainActivityContext) {
        super(itemCallback);
        this.context = context;
        this.mainActivityContext = mainActivityContext;
    }

    @NonNull
    @Override
    public HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_item, parent, false);
        return new HomeFragmentRecyclerViewItemViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder holder, int position) {
        Picasso.get().load(Uri.parse(getItem(position).getThumbnail())).into(holder.imageView);

        holder.linearLayout.setOnClickListener(v -> {
            mainActivityContext.navigateToVideos(getItem(position).getVideo_url());
        });
    }

    @Override
    public void onViewAttachedToWindow(@NonNull HomeFragmentRecyclerViewItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull HomeFragmentRecyclerViewItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
//        holder.releasePlayer();
    }

    @Override
    public void onViewRecycled(@NonNull HomeFragmentRecyclerViewItemViewHolder holder) {
        super.onViewRecycled(holder);
//        holder.releasePlayer();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public static class HomeFragmentRecyclerViewItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private CardView cardView;
        private LinearLayout linearLayout;

        public HomeFragmentRecyclerViewItemViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.video_item);
            imageView = itemView.findViewById(R.id.video_thumbnail);
            cardView = itemView.findViewById(R.id.video_card_frame);

            DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cardView.getLayoutParams();
            layoutParams.height = (int) (displayMetrics.heightPixels / 3.5);
            cardView.setLayoutParams(layoutParams);
        }
    }
}
