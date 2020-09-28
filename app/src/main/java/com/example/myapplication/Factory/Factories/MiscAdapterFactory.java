package com.example.myapplication.Factory.Factories;

import android.content.Context;
import androidx.recyclerview.widget.DiffUtil;
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter;
import com.example.myapplication.Adapters.MiscellaneousAdapter;
import com.example.myapplication.Adapters.RecentsAdapter;
import com.example.myapplication.Factory.CustomAdapters;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.Models.MovieModel;

import java.util.List;

public class MiscAdapterFactory extends CustomAdapters {

    @Override
    public HomeFragmentRecyclerViewAdapter getHomeFragmentAdapter(Context context, List<MovieModel> movies, MainActivityContext mainActivityContext) {
        return null;
    }

    @Override
    public RecentsAdapter getRecentAdapter(DiffUtil.ItemCallback<MovieModel> itemCallback, MainActivityContext mainActivityContext) {
        return null;
    }

    @Override
    public MiscellaneousAdapter getMiscAdapter(Context context) {
        return new MiscellaneousAdapter(context);
    }

}
