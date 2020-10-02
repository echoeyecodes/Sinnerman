package com.example.myapplication.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import com.example.myapplication.Models.VideoModel;
import org.jetbrains.annotations.NotNull;

public final class VideosItemCallback extends DiffUtil.ItemCallback<VideoModel> {
    private static VideosItemCallback videosItemCallback;

    private VideosItemCallback(){

    }

    public static VideosItemCallback newInstance() {
        if (videosItemCallback == null) {
            videosItemCallback = new VideosItemCallback();
        }
        return videosItemCallback;
    }

    @Override
    public boolean areItemsTheSame(@NonNull @NotNull VideoModel oldItem, @NonNull @NotNull VideoModel newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull @NotNull VideoModel oldItem, @NonNull @NotNull VideoModel newItem) {
        return oldItem.getLike_count() == newItem.getLike_count();
    }
}
