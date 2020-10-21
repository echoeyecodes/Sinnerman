package com.example.myapplication.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import com.example.myapplication.Models.VideoResponseBody;
import org.jetbrains.annotations.NotNull;

public final class VideosItemCallback extends DiffUtil.ItemCallback<VideoResponseBody> {
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
    public boolean areItemsTheSame(@NonNull @NotNull VideoResponseBody oldItem, @NonNull @NotNull VideoResponseBody newItem) {
        return oldItem.getVideo().getId().equals(newItem.getVideo().getId());
    }

    @Override
    public boolean areContentsTheSame(@NonNull @NotNull VideoResponseBody oldItem, @NonNull @NotNull VideoResponseBody newItem) {
        return oldItem.getVideo().getThumbnail().equals(newItem.getVideo().getVideo_url()) && oldItem.getVideo().getViews() == newItem.getVideo().getViews();
    }
}
