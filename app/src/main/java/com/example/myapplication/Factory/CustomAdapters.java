package com.example.myapplication.Factory;

import android.content.Context;
import androidx.recyclerview.widget.DiffUtil;
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter;
import com.example.myapplication.Adapters.MiscellaneousAdapter;
import com.example.myapplication.Adapters.RecentsAdapter;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.Models.MovieModel;

import java.util.List;

public abstract class CustomAdapters {
    public abstract HomeFragmentRecyclerViewAdapter getHomeFragmentAdapter(Context context, List<MovieModel> movies, MainActivityContext mainActivityContext);
    public abstract RecentsAdapter getRecentAdapter(DiffUtil.ItemCallback<MovieModel> itemCallback, MainActivityContext mainActivityContext);
    public abstract MiscellaneousAdapter getMiscAdapter(Context context);
}
