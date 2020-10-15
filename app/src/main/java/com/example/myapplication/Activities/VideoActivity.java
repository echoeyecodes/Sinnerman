package com.example.myapplication.Activities;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.R;
import com.example.myapplication.Singleton.VideoCacheSingleton;
import com.example.myapplication.Utils.CustomExoPlayerDataSourceFactory;
import com.example.myapplication.ViewModel.NetworkState;
import com.example.myapplication.ViewModel.VideoActivityViewModel;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

public class VideoActivity extends AppCompatActivity implements Player.EventListener, ValueAnimator.AnimatorUpdateListener {
    private PlayerView playerView;
    private ImageButton fullscreen_btn;
    private CircleImageView video_author_image;
    private TextView video_author_username;
    private TextView video_title;
    private TextView video_description;
    private ImageButton comment_btn;
    private ImageButton like_btn;
    private LinearLayout reload_btn;
    private SimpleExoPlayer player;
    private RelativeLayout loading_layout;
    private VideoActivityViewModel videoActivityViewModel;
    private ProgressBar progressBar;
    private ValueAnimator valueAnimator;
    private MediaSource mediaSource;
    private int currentWindow = 0;
    private long playBackPosition = 0;
    private String video_id;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_video);
        videoActivityViewModel = new ViewModelProvider(this).get(VideoActivityViewModel.class);
        playerView = findViewById(R.id.video_player_view);

        loading_layout = findViewById(R.id.video_activity_loading_layout);
        loading_layout.setBackgroundColor(R.color.black);

        video_author_image = findViewById(R.id.video_author_image);
        video_author_username = findViewById(R.id.video_author_username);
        video_title = findViewById(R.id.video_title);
        video_description = findViewById(R.id.video_description);

        progressBar = findViewById(R.id.exo_buffering);
        fullscreen_btn = findViewById(R.id.full_screen_btn);
        comment_btn = findViewById(R.id.comment_btn);
        like_btn = findViewById(R.id.like_btn);
        reload_btn = findViewById(R.id.retry_btn);

        video_id = getIntent().getStringExtra("video_id");

        valueAnimator = ValueAnimator.ofFloat(1,1.5f,1);
        valueAnimator.setDuration(500);

        valueAnimator.addUpdateListener(this);

        videoActivityViewModel.getIsFullScreen().observe(this, (isFullScreen) -> {
            if (isFullScreen) {
                adjustPlayerViewMargins(0);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                adjustPlayerViewMargins(getNavigationBarHeight());
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        comment_btn.setOnClickListener(v -> navigateToComments());
        reload_btn.setOnClickListener(v -> {
            refresh();
        });

        fullscreen_btn.setOnClickListener(v -> {
            toggleFullScreen(!videoActivityViewModel.getIsFullScreenValue());
        });

        like_btn.setOnClickListener(v -> animateLikeButton());

        videoActivityViewModel.getVideoObserver().observe(this, (video) ->{
            if(video != null){
                UserModel userModel = video.getUser();
                VideoModel videoModel = video.getVideo();

                Picasso.get().load(Uri.parse(userModel.getProfile_url())).into(video_author_image);
                video_author_username.setText("@".concat(userModel.getUsername()));
                video_title.setText(videoModel.getTitle());
                video_description.setText(videoModel.getDescription());

                mediaSource = buildMediaSource(Uri.parse(video.getVideo().getVideo_url()));
                initializePlayer();
            }
        });

        videoActivityViewModel.getRequest_status().observe(this, (status) ->{
            if(status == NetworkState.LOADING){
                loading_layout.setVisibility(View.VISIBLE);
            }else{
                loading_layout.setVisibility(View.GONE);
            }
        });

        videoActivityViewModel.setVideo_id(video_id);
    }

    private int getNavigationBarHeight(){
        Resources resources = Resources.getSystem();
        int resource = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if(resource > 0){
            return resources.getDimensionPixelSize(resource);
        }
        return 0;
    }

    private void animateLikeButton(){
        valueAnimator.start();
        like_btn.setColorFilter(R.color.colorPrimary);
    }
    private void adjustPlayerViewMargins(int margin){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
        params.setMargins(0,0,0, margin);
    }

    public void navigateToComments() {
        Intent intent = new Intent(this, CommentActivity.class);
        startActivity(intent);
    }

    private void toggleFullScreen(boolean value) {
        videoActivityViewModel.toggleFullScreen(value);
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
        player = new SimpleExoPlayer.Builder(this).build();
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
        DataSource.Factory dataSourceFactory = new CustomExoPlayerDataSourceFactory(this, 100 * 1024 * 1024, 5 * 1024 * 1024, VideoCacheSingleton.getVideoCache(this));
        HlsMediaSource.Factory hlsFactory = new HlsMediaSource.Factory(dataSourceFactory);
        return hlsFactory.createMediaSource(uri);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(Util.SDK_INT < 24){
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Util.SDK_INT >= 24){
            releasePlayer();
        }
    }

    @Override
    public void onBackPressed() {
        if (videoActivityViewModel.getIsFullScreenValue()) {
            toggleFullScreen(false);
            return;
        }
        super.onBackPressed();
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Util.SDK_INT >= 24){
            if(mediaSource != null){
                initializePlayer();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
        if(Util.SDK_INT < 24 || player == null){
            if(mediaSource != null){
                initializePlayer();
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        hideSystemUI();
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
    public void onPlayerError(@NotNull ExoPlaybackException error) {
        reload_btn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
            float value = (float) animation.getAnimatedValue();
            like_btn.setScaleX(value);
            like_btn.setScaleY(value);
    }
}
