package com.example.myapplication.Adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Interface.CommentActivityListener;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.CommentResponseBody;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.Paging.CommonListPagingListeners;
import com.example.myapplication.Paging.CommonListPagingViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.Utils.TimestampConverter;
import com.example.myapplication.viewmodel.NetworkState;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class CommentsAdapter extends ListAdapter<CommentResponseBody, CommentsAdapter.ViewHolder> implements CommonListPagingListeners {

    CommentActivityListener commentActivityListener;
    private NetworkState networkState;
    public CommentsAdapter(@NonNull DiffUtil.ItemCallback<CommentResponseBody> diffCallback, CommentActivityListener commentActivityListener) {
        super(diffCallback);
        this.commentActivityListener = commentActivityListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentResponseBody commentResponseBody = getItem(position);

        if (commentResponseBody == null) {
            holder.linearLayout.setVisibility(View.GONE);
            holder.handleNetworkStateChanged(networkState);
            return;
        }

        holder.linearLayout.setVisibility(View.VISIBLE);
        holder.getLoading_container().setVisibility(View.GONE);
        CommentModel commentModel = commentResponseBody.getComment();
        UserModel userModel = commentResponseBody.getUser();
        if(commentModel.getStatus() == 1){
            holder.progressBar.setVisibility(View.VISIBLE);
        }else{
            holder.progressBar.setVisibility(View.GONE);
        }

        holder.comment_author.setText(userModel.getFullname());
        holder.comment.setText(commentModel.getComment());
        holder.timestamp.setText(TimestampConverter.Companion.getInstance().convertToTimeDifference(commentModel.getCreatedAt()));

        Picasso.get().load(Uri.parse(userModel.getProfile_url())).into(holder.comment_author_image);
    }

    @Override
    public void retry() {

    }

    @Override
    public void onNetworkStateChanged(@NonNull NetworkState networkState) {
        this.networkState = networkState;
    }

    public static class ViewHolder extends CommonListPagingViewHolder {

        private final ProgressBar progressBar;
        private final TextView comment_author;
        private final TextView comment;
        private LinearLayout linearLayout;
        private final TextView timestamp;
        private final CircleImageView comment_author_image;

        public ViewHolder(View itemView, CommonListPagingListeners commonListPagingListeners){
            super(itemView, commonListPagingListeners);

            progressBar = itemView.findViewById(R.id.comment_status_indicator);
            comment = itemView.findViewById(R.id.comment);
            linearLayout = itemView.findViewById(R.id.comment_item_container);
            timestamp = itemView.findViewById(R.id.comment_timestamp);

            comment_author = itemView.findViewById(R.id.comment_author_name);
            comment_author_image = itemView.findViewById(R.id.comment_author_image);
        }

    }

}