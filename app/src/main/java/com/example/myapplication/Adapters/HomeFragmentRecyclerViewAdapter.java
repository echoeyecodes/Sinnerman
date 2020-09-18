package com.example.myapplication.Adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.*;

import com.example.myapplication.Models.MovieModel;
import com.example.myapplication.R;
import com.example.myapplication.Singleton.VideoCacheSingleton;
import com.example.myapplication.Utils.CustomExoPlayerDataSourceFactory;
import com.example.myapplication.Utils.HorizontalItemDecoration;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeFragmentRecyclerViewAdapter extends RecyclerView.Adapter<HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder> {
    private Context context;
    private List<MovieModel> movies;
    private static HomeFragmentRecyclerViewAdapter homeFragmentRecyclerViewAdapter;

    public HomeFragmentRecyclerViewAdapter(Context context, List<MovieModel> movies){
        this.movies = movies;
        this.context = context;
    }

    public static HomeFragmentRecyclerViewAdapter getAdapter(Context context, List<MovieModel> movies){
        if(homeFragmentRecyclerViewAdapter == null){
            homeFragmentRecyclerViewAdapter = new HomeFragmentRecyclerViewAdapter(context, movies);
        }
        return homeFragmentRecyclerViewAdapter;
    }

    @NonNull
    @Override
    public HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_home_recycler_view_item, parent, false);
        return new HomeFragmentRecyclerViewItemViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder holder, int position) {

        Uri uri = Uri.parse(movies.get(position).getThumbnail());
        MediaSource mediaSource = buildMediaSource(uri);
        holder.setMediaSource(mediaSource);
    }

    private MediaSource buildMediaSource(Uri uri){
        DataSource.Factory dataSourceFactory = new CustomExoPlayerDataSourceFactory(context, 100*1024*1024, 5*1024*1024, VideoCacheSingleton.getVideoCache(context));
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
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
    public int getItemCount() {
        return movies.size();
    }

    public static class HomeFragmentRecyclerViewItemViewHolder extends RecyclerView.ViewHolder implements Player.EventListener{
        private PlayerView playerView;
        private RelativeLayout overlay_layout;
        private SimpleExoPlayer player;
        private ImageView fullscreen_btn;
        private ProgressBar progressBar;
        private MediaSource mediaSource;
        private int currentWindow = 0;
        private long playBackPosition = 0;
        Context context;

        public HomeFragmentRecyclerViewItemViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;

            playerView = itemView.findViewById(R.id.video_play_view);
            overlay_layout = itemView.findViewById(R.id.video_play_view_overlay);
            fullscreen_btn = itemView.findViewById(R.id.exo_fullscreen);
            progressBar = itemView.findViewById(R.id.exo_buffering);

            fullscreen_btn.setOnClickListener(v -> {

            });
        }

        public void releasePlayer(){
            if(player != null){
                currentWindow = player.getCurrentWindowIndex();
                playBackPosition = player.getCurrentPosition();
                player.release();
                player = null;
            }
        }

        public void setMediaSource(MediaSource mediaSource) {
            this.mediaSource = mediaSource;
        }

        public void initializePlayer(){
            player = new SimpleExoPlayer.Builder(context).build();
            playerView.setPlayer(player);
            player.setPlayWhenReady(false);
            player.seekTo(currentWindow, playBackPosition);
            player.addListener(this);
            player.prepare(mediaSource, false, false);
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
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
