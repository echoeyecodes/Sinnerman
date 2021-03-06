package com.echoeyecodes.sinnerman.Activities;

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
import com.echoeyecodes.sinnerman.Fragments.ResolutionListFragment;
import com.echoeyecodes.sinnerman.Interface.VideoActivityListener;
import com.echoeyecodes.sinnerman.Models.ResolutionDimensions;
import com.echoeyecodes.sinnerman.Models.UserModel;
import com.echoeyecodes.sinnerman.Models.VideoModel;
import com.echoeyecodes.sinnerman.R;
import com.echoeyecodes.sinnerman.Utils.AuthenticationManager;
import com.echoeyecodes.sinnerman.Utils.IntegerToDp;
import com.echoeyecodes.sinnerman.viewmodel.NetworkState;
import com.echoeyecodes.sinnerman.viewmodel.VideoActivityViewModel;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceFactory;
import com.google.android.exoplayer2.source.hls.HlsManifest;
import com.google.android.exoplayer2.source.hls.playlist.HlsMasterPlaylist;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VideoActivity extends AppCompatActivity implements VideoActivityListener, Player.EventListener, ValueAnimator.AnimatorUpdateListener {
    private PlayerView playerView;
    private ImageButton fullscreen_btn;
    private ImageButton resolution_btn;
    private CircleImageView video_author_image;
    private TextView video_author_username;
    private TextView video_title;
    private TextView video_description;
    private ImageButton comment_btn;
    private ImageButton like_btn;
    private LinearLayout reload_btn;
    private SimpleExoPlayer player;
    private static final String RESOLUTION_FRAGMENT_TAG = "RESOLUTION_FRAGMENT_TAG";
    private RelativeLayout loading_layout;
    private VideoActivityViewModel videoActivityViewModel;
    private ProgressBar progressBar;
    private ValueAnimator valueAnimator;
    private MediaSource mediaSource;
    private int currentWindow = 0;
    private long playBackPosition = 0;
    private String video_id;

    @SuppressLint("ResourceAsColor")
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
        resolution_btn = findViewById(R.id.show_resolutions_btn);
        comment_btn = findViewById(R.id.comment_btn);
        like_btn = findViewById(R.id.like_btn);
        reload_btn = findViewById(R.id.video_retry_btn);


        Intent intent = getIntent();
//        String action = intent.getAction();
        Uri data = intent.getData();

        if (data != null) {
            AuthenticationManager authenticationManager = new AuthenticationManager();
            String token = authenticationManager.checkToken(this);
            if (token == null || token.equals("")) {
                authenticationManager.startAuthActivity(this);
                return;
            }
            video_id = data.getLastPathSegment();
        } else {
            video_id = getIntent().getStringExtra("video_id");
        }

        valueAnimator = ValueAnimator.ofFloat(1, 1.3f, 1);
        valueAnimator.setDuration(100);

        valueAnimator.addUpdateListener(this);

        videoActivityViewModel.isFullScreen().observe(this, (isFullScreen) -> {
            if (isFullScreen) {
                fullscreen_btn.setImageResource(R.drawable.ic_fullscreen_exit);
                adjustPlayerViewMargins(0);
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else {
                fullscreen_btn.setImageResource(R.drawable.ic_fullscreen);
                adjustPlayerViewMargins(getNavigationBarHeight());
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        comment_btn.setOnClickListener(v -> navigateToComments(video_id));
        reload_btn.setOnClickListener(v -> {
            refresh();
        });

        fullscreen_btn.setOnClickListener(v -> {
            toggleFullScreen(!videoActivityViewModel.getFullScreenValue());
        });

        like_btn.setOnClickListener(v -> animateLikeButton());

        videoActivityViewModel.getVideoObserver().observe(this, (video) -> {
            if (video != null) {
                UserModel userModel = video.getUser();
                VideoModel videoModel = video.getVideo();

                Picasso.get().load(Uri.parse(userModel.getProfile_url())).into(video_author_image);
                video_author_username.setText("@".concat(userModel.getUsername()));
                video_title.setText(videoModel.getTitle());
                video_description.setText(videoModel.getDescription());
                videoActivityViewModel.setSelectedItemPosition(0);
                mediaSource = buildMediaSource(Uri.parse(video.getVideo().getVideo_url()));
                initializePlayer();
            }
        });

        videoActivityViewModel.getRequest_status().observe(this, (status) -> {
            if (status == NetworkState.ERROR) {
                Toast.makeText(this, "Couldn't get resource. Please try again", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (status == NetworkState.LOADING) {
                loading_layout.setVisibility(View.VISIBLE);
            } else {
                loading_layout.setVisibility(View.GONE);
            }
        });

        videoActivityViewModel.getDidLike().observe(this, (like) -> {
            if (like) {
                like_btn.setBackgroundResource(R.drawable.like_icon);
            } else {
                like_btn.setBackgroundResource(R.drawable.unlike_icon);
            }
        });

        videoActivityViewModel.fetchVideo(video_id);

        resolution_btn.setOnClickListener(v -> {
            ResolutionListFragment.Companion.getInstance(videoActivityViewModel.getResolutions()).show(getSupportFragmentManager(), RESOLUTION_FRAGMENT_TAG);
        });
    }


    private int getNavigationBarHeight() {
        Resources resources = Resources.getSystem();
        int resource = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resource > 0) {
            return resources.getDimensionPixelSize(resource);
        }
        return 0;
    }

    private void animateLikeButton() {
        valueAnimator.start();
        videoActivityViewModel.likeVideo();
    }

    private void adjustPlayerViewMargins(int margin) {
        View controlView = findViewById(R.id.video_controls);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) controlView.getLayoutParams();
        params.setMargins(IntegerToDp.intToDp(10), IntegerToDp.intToDp(10), IntegerToDp.intToDp(10), margin);
        controlView.setLayoutParams(params);
    }

    public void navigateToComments(String id) {
        Intent intent = new Intent(this, CommentActivity.class);
        intent.putExtra("video_id", id);
        startActivity(intent);
    }

    private void toggleFullScreen(boolean value) {
        videoActivityViewModel.toggleFullScreen(value);
    }

    public void releasePlayer() {
        if (player != null) {
            currentWindow = player.getCurrentWindowIndex();
            playBackPosition = player.getCurrentPosition();
            player.setPlayWhenReady(false);
            player.stop();
            player.release();
            player.removeListener(this);
            playerView.setPlayer(null);
            player = null;
        }
    }

    public void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this).setTrackSelector(videoActivityViewModel.getDefaultTrackSelector()).build();
        playerView.setPlayer(player);
        player.setPlayWhenReady(true);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);
        player.setMediaSource(mediaSource, playBackPosition);
        player.prepare();
        player.play();
    }

    private void changeResolution(int position, ResolutionDimensions dimensions) {
        if (position == 0) {
            videoActivityViewModel.getDefaultTrackSelector().setParameters(DefaultTrackSelector.Parameters.getDefaults(this));
        } else {
            DefaultTrackSelector.Parameters parameters = new DefaultTrackSelector.ParametersBuilder(this).clearSelectionOverrides().
                    clearVideoSizeConstraints().setMinVideoSize(dimensions.getWidth(), dimensions.getHeight())
                    .setMaxVideoSize(dimensions.getWidth(), dimensions.getHeight()).build();
            videoActivityViewModel.getDefaultTrackSelector().setParameters(parameters);
        }

        videoActivityViewModel.setSelectedItemPosition(position);
        ResolutionListFragment resolutionListFragment = (ResolutionListFragment) getSupportFragmentManager().findFragmentByTag(RESOLUTION_FRAGMENT_TAG);
        if (resolutionListFragment != null && resolutionListFragment.isVisible()) {
            resolutionListFragment.dismiss();
        }
    }

    private void refresh() {
        reload_btn.setVisibility(View.GONE);
        releasePlayer();
        initializePlayer();
    }

    private MediaSource buildMediaSource(Uri uri) {
        HttpDataSource.Factory httpDataSource = new DefaultHttpDataSourceFactory(Util.getUserAgent(this, getPackageName()), null);
        CacheDataSource.Factory cacheDataSource = new CacheDataSource.Factory().setCache(videoActivityViewModel.getSimpleCache()).setUpstreamDataSourceFactory(httpDataSource);
        MediaSourceFactory mediaSourceFactory = new DefaultMediaSourceFactory(cacheDataSource);
        return mediaSourceFactory.createMediaSource(MediaItem.fromUri(uri));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT < 24) {
            releasePlayer();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT >= 24) {
            releasePlayer();
        }
    }

    @Override
    public void onBackPressed() {
        if (videoActivityViewModel.getFullScreenValue()) {
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
        if (Util.SDK_INT >= 24) {
            if (mediaSource != null) {
                initializePlayer();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
        if (Util.SDK_INT < 24 || player == null) {
            if (mediaSource != null) {
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
    public void onPlaybackStateChanged(int state) {
        if (state == Player.STATE_BUFFERING) {
            playerView.showController();
            progressBar.setVisibility(View.VISIBLE);
        } else if (state == Player.STATE_READY) {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onTimelineChanged(@NotNull Timeline timeline, int reason) {
        Object manifest = player.getCurrentManifest();
        ArrayList<ResolutionDimensions> resolutions = new ArrayList<>();
        resolutions.add(new ResolutionDimensions(0, 0));

        if (manifest != null) {
            HlsManifest hlsManifest = (HlsManifest) manifest;
            List<HlsMasterPlaylist.Variant> variableDefinitions = hlsManifest.masterPlaylist.variants;

            for (HlsMasterPlaylist.Variant trackSelection : variableDefinitions) {
                if (trackSelection != null) {
                    ResolutionDimensions dimensions = new ResolutionDimensions(trackSelection.format.width, trackSelection.format.height);
                    resolutions.add(dimensions);
                }
            }
            videoActivityViewModel.setResolutions(resolutions);
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

    @Override
    public void onResolutionSelected(int position, ResolutionDimensions resolutionDimensions) {
        changeResolution(position, resolutionDimensions);
    }
}
