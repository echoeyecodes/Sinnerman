package com.example.myapplication.Adapters;

import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

public class RecentsAdapter extends ListAdapter<VideoModel, RecentsAdapter.RecentsViewHolder> {
private final MainActivityContext mainActivityContext;

    public RecentsAdapter(@NonNull @NotNull DiffUtil.ItemCallback<VideoModel> diffCallback, MainActivityContext mainActivityContext) {
        super(diffCallback);
        this.mainActivityContext = mainActivityContext;
    }
    @NonNull
    @NotNull
    @Override
    public RecentsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_product_item, parent, false);
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        layoutParams.width = displayMetrics.widthPixels / 3;
        layoutParams.height = displayMetrics.heightPixels / 3;
        view.setLayoutParams(layoutParams);
        return new RecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecentsViewHolder holder, int position) {

        Picasso.get().load(Uri.parse("https://res.cloudinary.com/echoeyecodes/video/upload/v1600686482/eib6rz0mzjlqw0rp1vfc.jpg")).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    protected static class RecentsViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;
        private CardView cardView;
        public RecentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recents_image_placeholder);
            cardView = itemView.findViewById(R.id.recents_videos_item);
        }
    }

}
