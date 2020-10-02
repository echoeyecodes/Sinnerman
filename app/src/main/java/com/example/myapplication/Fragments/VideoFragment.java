package com.example.myapplication.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Singleton.VideoCacheSingleton;
import com.example.myapplication.Utils.CustomExoPlayerDataSourceFactory;
import com.example.myapplication.ViewModel.MainActivityViewModel;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VideoFragment extends Fragment implements Player.EventListener {
    private PlayerView playerView;
    private MainActivityContext mainActivityContext;
    private ImageButton fullscreen_btn;
    private LinearLayout reload_btn;
    MainActivityViewModel mainActivityViewModel;
    private SimpleExoPlayer player;
    private ProgressBar progressBar;
    private MediaSource mediaSource;
    private int currentWindow = 0;
    private long playBackPosition = 0;
    private Uri video_url;

    //Required public constructor
    public VideoFragment() {

    }

    public VideoFragment(String video_url) {
        this.video_url = Uri.parse(video_url);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        playerView = view.findViewById(R.id.video_player_view);
        progressBar = view.findViewById(R.id.exo_buffering);
        fullscreen_btn = view.findViewById(R.id.full_screen_btn);
        reload_btn = view.findViewById(R.id.retry_btn);
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        fullscreen_btn.setOnClickListener(v -> {
            mainActivityContext.toggleFullScreen(!mainActivityViewModel.getIsFullScreenValue());
        });
        reload_btn.setOnClickListener(v -> {
            refresh();
        });
        mediaSource = buildMediaSource(video_url);
    }

    public void releasePlayer() {
        if (player != null) {
            currentWindow = player.getCurrentWindowIndex();
            playBackPosition = player.getCurrentPosition();
            player.release();
            player.removeListener(this);
            player = null;
        }
    }

    public void initializePlayer() {
        player = new SimpleExoPlayer.Builder(Objects.requireNonNull(getContext())).build();
        playerView.setPlayer(player);
        player.setPlayWhenReady(true);
        player.seekTo(currentWindow, playBackPosition);
        player.addListener(this);
        player.prepare(mediaSource, false, false);
    }

    private void refresh() {
        reload_btn.setVisibility(View.GONE);
        releasePlayer();
        initializePlayer();
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new CustomExoPlayerDataSourceFactory(getContext(), 100 * 1024 * 1024, 5 * 1024 * 1024, VideoCacheSingleton.getVideoCache(getContext()));
        HlsMediaSource.Factory hlsFactory = new HlsMediaSource.Factory(dataSourceFactory);
        return hlsFactory.createMediaSource(uri);
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        mainActivityContext = (MainActivityContext) context;
        if (!(mainActivityContext instanceof MainActivity)) {
            try {
                throw new Exception("You must implement MainActivityContext class");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializePlayer();
    }


    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_ENDED) {
            player.prepare(mediaSource, true, true);
        }
        if (playbackState == Player.STATE_BUFFERING) {
            playerView.showController();
            progressBar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        reload_btn.setVisibility(View.VISIBLE);
    }
}
