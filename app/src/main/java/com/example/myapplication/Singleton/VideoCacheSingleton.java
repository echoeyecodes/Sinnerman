package com.example.myapplication.Singleton;

import android.content.Context;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;

import java.io.File;

public class VideoCacheSingleton {
    private static SimpleCache simpleCache;

    public static SimpleCache getVideoCache(Context context){
        if(simpleCache ==null){
            LeastRecentlyUsedCacheEvictor leastRecentlyUsedCacheEvictor = new LeastRecentlyUsedCacheEvictor(100*1024*1024);
            simpleCache = new SimpleCache(new File(context.getCacheDir(), "media"), leastRecentlyUsedCacheEvictor, new ExoDatabaseProvider(context));
        }
        return simpleCache;
    }
}
