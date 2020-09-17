package com.example.myapplication;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

public class MovieDetailsViewModel extends AndroidViewModel {
    private boolean isFullScreen = false;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private SimpleCache simpleCache;
    HandlerThread handlerThread;
    Handler handler;

    public MovieDetailsViewModel(@NonNull Application application) {
        super(application);
        handlerThread = new HandlerThread("Movie Details Handler Thread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(100*1024*1024);
        simpleCache = new SimpleCache(new File(application.getApplicationContext().getCacheDir(), "media"), leastRecentlyUsedCacheEvictor, new ExoDatabaseProvider(application.getApplicationContext()));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        resetCache();
    }

    public void resetCache(){
        Log.d("CAR", "CACHE RELEASED");
        simpleCache.release();
        simpleCache = null;
    }

    public boolean getIsFullScreen(){
        return  isFullScreen;
    }

    public Handler getHandler(){
        return handler;
    }

    public void setFullScreen(boolean isFullScreen){
        this.isFullScreen = isFullScreen;
    }

    public boolean isPlayWhenReady() {
        return playWhenReady;
    }

    public void setPlayWhenReady(boolean playWhenReady) {
        this.playWhenReady = playWhenReady;
    }

    public SimpleCache getSimpleCache() {
        return simpleCache;
    }

    public int getCurrentWindow() {
        return currentWindow;
    }

    public void setCurrentWindow(int currentWindow) {
        this.currentWindow = currentWindow;
    }

    public long getPlaybackPosition() {
        return playbackPosition;
    }

    public void setPlaybackPosition(long playbackPosition) {
        this.playbackPosition = playbackPosition;
    }
}
