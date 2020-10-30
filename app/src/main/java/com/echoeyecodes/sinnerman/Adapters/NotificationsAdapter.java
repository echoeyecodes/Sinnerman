package com.echoeyecodes.sinnerman.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import com.bumptech.glide.Glide;
import com.echoeyecodes.sinnerman.Interface.MainActivityContext;
import com.echoeyecodes.sinnerman.Interface.NotificationFragmentListener;
import com.echoeyecodes.sinnerman.Models.UploadNotificationModel;
import com.echoeyecodes.sinnerman.Paging.CommonListPagingListeners;
import com.echoeyecodes.sinnerman.Paging.CommonListPagingViewHolder;
import com.echoeyecodes.sinnerman.R;
import com.echoeyecodes.sinnerman.Utils.TimestampConverter;
import com.echoeyecodes.sinnerman.viewmodel.NetworkState;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

public class NotificationsAdapter extends ListAdapter<UploadNotificationModel, NotificationsAdapter.RecentsViewHolder> implements CommonListPagingListeners {
private final MainActivityContext mainActivityContext;
private NetworkState networkState;
private final NotificationFragmentListener notificationFragmentListener;
private final Context context;

    public NotificationsAdapter(Context context, @NonNull @NotNull DiffUtil.ItemCallback<UploadNotificationModel> diffCallback, MainActivityContext mainActivityContext, NotificationFragmentListener notificationFragmentListener) {
        super(diffCallback);
        this.mainActivityContext = mainActivityContext;
        this.notificationFragmentListener = notificationFragmentListener;
        this.context = context.getApplicationContext();
    }

    @NonNull
    @NotNull
    @Override
    public RecentsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_notification_item, parent, false);
        return new RecentsViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecentsViewHolder holder, int position) {
        UploadNotificationModel uploadNotificationModel = getItem(position);

        if(uploadNotificationModel == null){
            holder.linearLayout.setVisibility(View.GONE);
            holder.handleNetworkStateChanged(networkState);
            return;
        }
        holder.linearLayout.setVisibility(View.VISIBLE);
        holder.getLoading_container().setVisibility(View.GONE);
        Glide.with(context).load(Uri.parse(uploadNotificationModel.getThumbnail())).into(holder.image_thumbnail);
        Glide.with(context).load(Uri.parse(uploadNotificationModel.getProfile_url())).into(holder.circleImageView);
        holder.message.setText(uploadNotificationModel.getMessage());
        holder.timestamp.setText(TimestampConverter.Companion.getInstance().convertToTimeDifference(uploadNotificationModel.getTimestamp()));

        holder.linearLayout.setOnClickListener(v -> mainActivityContext.navigateToVideos(uploadNotificationModel.getVideo_id()));
    }

    @Override
    public void retry() {
        notificationFragmentListener.retry();
    }

    @Override
    public void onNetworkStateChanged(@NonNull NetworkState networkState) {
        this.networkState = networkState;
    }

    protected static class RecentsViewHolder extends CommonListPagingViewHolder {
        private final ImageView image_thumbnail;
        private final LinearLayout linearLayout;
        private final TextView timestamp;
        private final TextView message;
        private final CircleImageView circleImageView;

        public RecentsViewHolder(@NonNull @NotNull View itemView, NotificationsAdapter notificationsAdapter) {
            super(itemView, notificationsAdapter);

            image_thumbnail = itemView.findViewById(R.id.notification_thumbnail);
            linearLayout = itemView.findViewById(R.id.notification_item);
            timestamp = itemView.findViewById(R.id.notification_timestamp);
            message = itemView.findViewById(R.id.notification_message);
            circleImageView = itemView.findViewById(R.id.notification_author);
        }
    }

}
