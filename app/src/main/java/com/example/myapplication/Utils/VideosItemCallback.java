package com.example.myapplication.Utils;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import com.example.myapplication.Models.MovieModel;
import org.jetbrains.annotations.NotNull;

public final class VideosItemCallback extends DiffUtil.ItemCallback<MovieModel> {
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
    public boolean areItemsTheSame(@NonNull @NotNull MovieModel oldItem, @NonNull @NotNull MovieModel newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull @NotNull MovieModel oldItem, @NonNull @NotNull MovieModel newItem) {
        return oldItem.getLike_count() == newItem.getLike_count();
    }
}
