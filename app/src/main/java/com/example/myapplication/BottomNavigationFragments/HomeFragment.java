package com.example.myapplication.BottomNavigationFragments;

import android.os.Bundle;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter;
import com.example.myapplication.Factory.Factories.HomeVideoAdapterFactory;
import com.example.myapplication.Factory.FactoryGenerator;
import com.example.myapplication.Factory.TYPE;
import com.example.myapplication.Models.MovieModel;
import com.example.myapplication.R;

import java.util.Arrays;

public class HomeFragment extends Fragment  {

    private static HomeFragment homeFragment;
    private RecyclerView recyclerView;

    public static HomeFragment newInstance(){
        if(homeFragment == null){
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.fragment_home_recycler_view);
        HomeVideoAdapterFactory homeVideoAdapterFactory = (HomeVideoAdapterFactory) FactoryGenerator.getFactory(TYPE.VIDEOS);
        MovieModel movieModel = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1598001691/t5kq8fodguw1khit88hc.mp4");
        MovieModel movieModel1 = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/q_50/v1600014910/qefhiqfksi6mwrvdxxau.mp4");
        assert homeVideoAdapterFactory != null;
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        HomeFragmentRecyclerViewAdapter adapter = homeVideoAdapterFactory.getHomeFragmentAdapter(getContext(), Arrays.asList(movieModel, movieModel1));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}