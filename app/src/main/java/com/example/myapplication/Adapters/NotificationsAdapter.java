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
import com.example.myapplication.Models.NotificationModel;
import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

public class NotificationsAdapter extends ListAdapter<NotificationModel, NotificationsAdapter.RecentsViewHolder> {
private final MainActivityContext mainActivityContext;

    public NotificationsAdapter(@NonNull @NotNull DiffUtil.ItemCallback<NotificationModel> diffCallback, MainActivityContext mainActivityContext) {
        super(diffCallback);
        this.mainActivityContext = mainActivityContext;
    }
    @NonNull
    @NotNull
    @Override
    public RecentsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item, parent, false);
        return new RecentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecentsViewHolder holder, int position) {
        Picasso.get().load(Uri.parse("https://res.cloudinary.com/echoeyecodes/video/upload/v1600686482/eypac8l0uvf5m9rtkqpf.jpg")).into(holder.image_thumbnail);
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    protected static class RecentsViewHolder extends RecyclerView.ViewHolder{
        private final ImageView image_thumbnail;
        public RecentsViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            image_thumbnail = itemView.findViewById(R.id.notifications_thumbnail);
        }
    }

}
