package com.example.myapplication.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Interface.CommentActivityListener;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.CommentResponseBody;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.R;

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
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar progressBar;

        public ViewHolder(View itemView){
            super(itemView);

            progressBar = itemView.findViewById(R.id.comment_status_indicator);
        }

    }

}