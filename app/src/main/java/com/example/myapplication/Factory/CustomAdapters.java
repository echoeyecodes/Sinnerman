package com.example.myapplication.Factory;

import android.content.Context;
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter;
import com.example.myapplication.Models.MovieModel;

import java.util.List;

public abstract class CustomAdapters {

    public abstract HomeFragmentRecyclerViewAdapter getHomeFragmentAdapter(Context context, List<MovieModel> movies);

}
