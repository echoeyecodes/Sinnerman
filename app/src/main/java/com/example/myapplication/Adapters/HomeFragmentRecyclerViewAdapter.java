package com.example.myapplication.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.*;

import androidx.recyclerview.widget.ListAdapter;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.Models.MovieModel;
import com.example.myapplication.R;
import com.example.myapplication.Singleton.VideoCacheSingleton;
import com.example.myapplication.Utils.CustomExoPlayerDataSourceFactory;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.*;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeFragmentRecyclerViewAdapter extends ListAdapter<MovieModel, HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder> implements Serializable {
    private transient final Context context;
    private transient final MainActivityContext mainActivityContext;

    public HomeFragmentRecyclerViewAdapter(DiffUtil.ItemCallback<MovieModel> itemCallback, Context context, MainActivityContext mainActivityContext) {
        super(itemCallback);
        this.context = context;
        this.mainActivityContext = mainActivityContext;
    }

    @NonNull
    @Override
    public HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_recycler_view_item, parent, false);
        return new HomeFragmentRecyclerViewItemViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder holder, int position) {
        holder.like_count.setText(String.valueOf(getItem(position).getLike_count()));
        holder.comment_btn.setOnClickListener(v -> mainActivityContext.openComments());
        Uri uri = Uri.parse(getItem(position).getThumbnail());
        MediaSource mediaSource = buildMediaSource(uri);
        holder.setMediaSource(mediaSource);

        holder.like_btn.setOnClickListener(v -> mainActivityContext.likeVideo(getItem(position)));
    }

    private MediaSource buildMediaSource(Uri uri){
        DataSource.Factory dataSourceFactory = new CustomExoPlayerDataSourceFactory(context, 100*1024*1024, 5*1024*1024, VideoCacheSingleton.getVideoCache(context));
        HlsMediaSource.Factory hlsFactory = new HlsMediaSource.Factory(dataSourceFactory);
            return hlsFactory.createMediaSource(uri);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull HomeFragmentRecyclerViewItemViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.initializePlayer();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull HomeFragmentRecyclerViewItemViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.releasePlayer();
    }

    @Override
    public void onViewRecycled(@NonNull HomeFragmentRecyclerViewItemViewHolder holder) {
        super.onViewRecycled(holder);
        holder.releasePlayer();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public static class HomeFragmentRecyclerViewItemViewHolder extends RecyclerView.ViewHolder implements Player.EventListener{
        private final PlayerView playerView;
        private SimpleExoPlayer player;
        private final ProgressBar progressBar;
        private MediaSource mediaSource;
        private final ImageButton comment_btn;
        private final ImageButton like_btn;
        private final TextView like_count;
        private int currentWindow = 0;
        private long playBackPosition = 0;
        Context context;

        public HomeFragmentRecyclerViewItemViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            playerView = itemView.findViewById(R.id.video_play_view);
            comment_btn = itemView.findViewById(R.id.comment_btn);
            like_btn = itemView.findViewById(R.id.like_btn);
            like_count = itemView.findViewById(R.id.like_count);
            progressBar = itemView.findViewById(R.id.exo_buffering);
        }

        public void releasePlayer(){
            if(player != null){
                currentWindow = player.getCurrentWindowIndex();
                playBackPosition = player.getCurrentPosition();
                player.release();
                player.removeListener(this);
                player = null;
            }
        }
        public void setMediaSource(MediaSource mediaSource) {
            this.mediaSource = mediaSource;
        }

        public void initializePlayer(){
            player = new SimpleExoPlayer.Builder(context).build();
            playerView.setPlayer(player);
            player.setPlayWhenReady(true);
            player.seekTo(currentWindow, playBackPosition);
            player.addListener(this);
            player.prepare(mediaSource, true, true);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if(playbackState == Player.STATE_ENDED){
                player.prepare(mediaSource, true, true);
            }
            if(playbackState == Player.STATE_BUFFERING){
                playerView.showController();
                progressBar.setVisibility(View.VISIBLE);
            }else if(playbackState == Player.STATE_READY){
                if(progressBar.getVisibility() == View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                }
            }
        }
    }
}
