package com.example.myapplication.Utils;

import android.content.Context;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.upstream.*;
import com.google.android.exoplayer2.upstream.cache.CacheDataSink;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

public class CustomExoPlayerDataSourceFactory implements DataSource.Factory {
    private final Context context;
    private final DefaultDataSourceFactory defaultDatasourceFactory;
    private final long maxFileSize, maxCacheSize;
    private final SimpleCache cache;

    public CustomExoPlayerDataSourceFactory(Context context, long maxCacheSize, long maxFileSize, SimpleCache cache){

        this.context = context;
        this.maxCacheSize = maxCacheSize;
        this.maxFileSize = maxFileSize;
        this.cache = cache;
        String useragent = Util.getUserAgent(context, "movie-app");
        defaultDatasourceFactory = new DefaultDataSourceFactory(context, useragent);
    }


    @Override
    public DataSource createDataSource() {
        return new CacheDataSource(cache, defaultDatasourceFactory.createDataSource(), new FileDataSource(), new CacheDataSink(cache, maxFileSize), CacheDataSource.FLAG_BLOCK_ON_CACHE | CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR, null);
    }
}
