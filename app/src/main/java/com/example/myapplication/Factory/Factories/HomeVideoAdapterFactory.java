package com.example.myapplication.Factory.Factories;

import android.content.Context;
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter;
import com.example.myapplication.Factory.CustomAdapters;
import com.example.myapplication.Models.MovieModel;

import java.util.List;

public class HomeVideoAdapterFactory extends CustomAdapters {

    @Override
    public HomeFragmentRecyclerViewAdapter getHomeFragmentAdapter(Context context, List<MovieModel> movies) {
        return HomeFragmentRecyclerViewAdapter.getAdapter(context, movies);
    }
}
