package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.R;

public class CommentsAdapter extends ListAdapter<CommentModel, CommentsAdapter.ViewHolder> {


    public CommentsAdapter(@NonNull DiffUtil.ItemCallback<CommentModel> diffCallback) {
        super(diffCallback);
    }

    @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 7;
        }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.layout_comment_item, parent, false));
        }
    }

}