package com.example.myapplication.Adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class CommentsAdapter extends ListAdapter<CommentResponseBody, CommentsAdapter.ViewHolder> {

    CommentActivityListener commentActivityListener;
    public CommentsAdapter(@NonNull DiffUtil.ItemCallback<CommentResponseBody> diffCallback, CommentActivityListener commentActivityListener) {
        super(diffCallback);
        this.commentActivityListener = commentActivityListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onCurrentListChanged(@NonNull List<CommentResponseBody> previousList, @NonNull List<CommentResponseBody> currentList) {
        super.onCurrentListChanged(previousList, currentList);
        commentActivityListener.onCommentInserted();
    }

    @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentModel commentModel = getItem(position).getComment();
        UserModel userModel = getItem(position).getUser();

        if(commentModel.getStatus() == 1){
            holder.progressBar.setVisibility(View.VISIBLE);
        }else{
            holder.progressBar.setVisibility(View.GONE);
        }

        holder.comment_author.setText(userModel.getFullname());
        holder.comment.setText(commentModel.getComment());

        Picasso.get().load(Uri.parse(userModel.getProfile_url())).into(holder.comment_author_image);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ProgressBar progressBar;
        private final TextView comment_author;
        private final TextView comment;
        private final CircleImageView comment_author_image;

        public ViewHolder(View itemView){
            super(itemView);

            progressBar = itemView.findViewById(R.id.comment_status_indicator);
            comment = itemView.findViewById(R.id.comment);

            comment_author = itemView.findViewById(R.id.comment_author_name);
            comment_author_image = itemView.findViewById(R.id.comment_author_image);
        }

    }

}