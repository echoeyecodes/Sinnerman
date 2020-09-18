package com.example.myapplication.BottomNavigationFragments;

import android.content.Context;
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
import com.example.myapplication.Interface.ToggleFullScreen;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Models.MovieModel;
import com.example.myapplication.R;

import java.util.Arrays;

public class HomeFragment extends Fragment  {

    private static HomeFragment homeFragment;
    private RecyclerView recyclerView;
    private HomeFragmentRecyclerViewAdapter adapter;
    private ToggleFullScreen toggleFullScreen;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        toggleFullScreen = (ToggleFullScreen) context;
        if(!(toggleFullScreen instanceof MainActivity)){
            try {
                throw new Exception("You need to implement Toggle Full Screen");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.fragment_home_recycler_view);
        HomeVideoAdapterFactory homeVideoAdapterFactory = (HomeVideoAdapterFactory) FactoryGenerator.getFactory(TYPE.VIDEOS);
        MovieModel movieModel = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1598001691/t5kq8fodguw1khit88hc.mp4");
        MovieModel movieModel1 = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/q_50/v1600014910/qefhiqfksi6mwrvdxxau.mp4");

        MovieModel test = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1600014910/qefhiqfksi6mwrvdxxau.m3u8");
        MovieModel test1 = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1600349759/lbznfgkewkthl0gkrclf.m3u8");
        assert homeVideoAdapterFactory != null;
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        adapter = homeVideoAdapterFactory.getHomeFragmentAdapter(getContext(), Arrays.asList(test, test1), toggleFullScreen);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onStop() {
        super.onStop();
        adapter = null;
        toggleFullScreen = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(adapter != null){
            int size = adapter.getItemCount();
            for(int item = 0; item< size; item++){
                HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder viewHolder = (HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder) recyclerView.findViewHolderForAdapterPosition(item);
                if(viewHolder != null){
                    viewHolder.releasePlayer();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(adapter != null){
            int size = adapter.getItemCount();
            for(int item = 0; item< size; item++){
                HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder viewHolder = (HomeFragmentRecyclerViewAdapter.HomeFragmentRecyclerViewItemViewHolder) recyclerView.findViewHolderForAdapterPosition(item);
                if(viewHolder != null){
                    viewHolder.initializePlayer();
                }
            }
        }
    }
}