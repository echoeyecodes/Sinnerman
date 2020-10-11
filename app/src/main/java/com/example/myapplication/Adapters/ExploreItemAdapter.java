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
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class ExploreItemAdapter extends ListAdapter<VideoModel, ExploreItemAdapter.ExploreItemViewHolder> implements Serializable {
    private final Context context;
    private final MainActivityContext mainActivityContext;
    private ExploreItemViewHolder exploreItemViewHolder;

    public ExploreItemAdapter(DiffUtil.ItemCallback<VideoModel> itemCallback, Context context, MainActivityContext mainActivityContext) {
        super(itemCallback);
        this.context = context;
        this.mainActivityContext = mainActivityContext;

    }

    @NonNull
    @Override
    public ExploreItemAdapter.ExploreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_feed_item, parent, false);
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        LinearLayout linearLayout = view.findViewById(R.id.video_item);
        CardView cardView = view.findViewById(R.id.video_card_frame);

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) linearLayout.getLayoutParams();
        LinearLayout.LayoutParams cardViewLayoutParams = (LinearLayout.LayoutParams) cardView.getLayoutParams();
        layoutParams.width = (int) (displayMetrics.widthPixels * 0.8);
        cardViewLayoutParams.height = (int) (displayMetrics.heightPixels / 3.5);

        cardView.setLayoutParams(cardViewLayoutParams);
        linearLayout.setLayoutParams(layoutParams);
        exploreItemViewHolder = new ExploreItemViewHolder(view);
        return exploreItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreItemAdapter.ExploreItemViewHolder holder, int position) {
        Picasso.get().load(Uri.parse(getItem(position).getThumbnail())).into(holder.imageView);

        holder.linearLayout.setOnClickListener(v -> {
            mainActivityContext.navigateToVideos(getItem(position).getVideo_url());
        });
    }

    public static class ExploreItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private CardView cardView;
        private LinearLayout linearLayout;

        public ExploreItemViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.video_item);
            imageView = itemView.findViewById(R.id.video_thumbnail);
            cardView = itemView.findViewById(R.id.video_card_frame);

        }
    }
}