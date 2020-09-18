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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.*;

import com.example.myapplication.Interface.ToggleFullScreen;
import com.example.myapplication.Models.MovieModel;
import com.example.myapplication.R;
import com.example.myapplication.Singleton.VideoCacheSingleton;
import com.example.myapplication.Utils.CustomExoPlayerDataSourceFactory;
import com.example.myapplication.Utils.HorizontalItemDecoration;
import com.example.myapplication.ViewModel.MainActivityViewModel;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroup;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.manifest.DashManifest;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.source.hls.playlist.HlsMediaPlaylist;
import com.google.android.exoplayer2.trackselection.*;
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
    private ToggleFullScreen toggleFullScreen;
    private static MainActivityViewModel mainActivityViewModel;

    public HomeFragmentRecyclerViewAdapter(Context context, List<MovieModel> movies, ToggleFullScreen toggleFullScreen){
        this.movies = movies;
        this.context = context;
        this.toggleFullScreen = toggleFullScreen;
    }

    public static HomeFragmentRecyclerViewAdapter getAdapter(Context context, List<MovieModel> movies, ToggleFullScreen toggleFullScreen){
        if(homeFragmentRecyclerViewAdapter == null){
            homeFragmentRecyclerViewAdapter = new HomeFragmentRecyclerViewAdapter(context, movies, toggleFullScreen);
            mainActivityViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(MainActivityViewModel.class);
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
        mainActivityViewModel.getIsFullScreen().observe((LifecycleOwner) context, v ->{
            if(v){
                holder.fullscreen_btn.setImageDrawable(context.getDrawable(R.drawable.exo_controls_fullscreen_exit));
            }else {
                holder.fullscreen_btn.setImageDrawable(context.getDrawable(R.drawable.exo_controls_fullscreen_enter));
            }
        });

        holder.fullscreen_btn.setOnClickListener(v -> toggleFullScreen.toggleFullScreen(mainActivityViewModel.getIsFullScreen().getValue()));
        Uri uri = Uri.parse(movies.get(position).getThumbnail());
        MediaSource mediaSource = buildMediaSource(uri);
        holder.setMediaSource(mediaSource);
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
            DefaultTrackSelector defaultTrackSelector = new DefaultTrackSelector(context);
            defaultTrackSelector.setParameters(defaultTrackSelector.buildUponParameters().setMaxVideoSize(144, 144));
            player = new SimpleExoPlayer.Builder(context).setTrackSelector(defaultTrackSelector).build();
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

        @Override
        public void onTracksChanged(@NonNull TrackGroupArray trackGroups, @NonNull TrackSelectionArray trackSelections) {
//                for(int i = 0; i<trackGroups.length; i++){
//                    TrackGroup trackGroup = trackGroups.get(i);
//                    for(int j=0; j<trackGroup.length; j++){
//                        Log.d("VIDEOSSS", ""+trackGroup.getFormat(j));
//                    }
//                }

            for(int i = 0; i<trackSelections.length; i++){
                TrackSelection trackSelection = trackSelections.get(i);
                if(trackSelection != null) {
                    for(int j =0; j< trackSelection.length(); j++){
                        Log.d("VIDEOSSS", ""+trackSelection.getFormat(j).height);
                    }
                }
            }
        }


        //Called when the manifest is loaded
        @Override
        public void onTimelineChanged(Timeline timeline, int reason) {

                Object manifest = player.getCurrentManifest();
                if(manifest != null){
                    HlsManifest hlsManifest = (HlsManifest) manifest;
                }
        }
    }
}
