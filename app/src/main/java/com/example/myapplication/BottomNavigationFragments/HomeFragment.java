package com.example.myapplication.BottomNavigationFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.*;
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter;
import com.example.myapplication.Factory.Factories.HomeVideoAdapterFactory;
import com.example.myapplication.Factory.FactoryGenerator;
import com.example.myapplication.Factory.TYPE;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Models.MovieModel;
import com.example.myapplication.R;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class HomeFragment extends Fragment  {

    private static HomeFragment homeFragment;
    private RecyclerView recyclerView;
    private HomeFragmentRecyclerViewAdapter adapter;
    private MainActivityContext mainActivityContext;
    private static final String PARCEABLE_LAYOUT_MANAGER_KEY = "PARCEABLE_LAYOUT_MANAGER_KEY";
    LinearLayoutManager linearLayoutManager;
    Parcelable parcelable;

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
        mainActivityContext = (MainActivityContext) context;
        if(!(mainActivityContext instanceof MainActivity)){
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

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        MovieModel test = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1600686480/xf6knnaxf6yca9lzgz7d.m3u8");
        MovieModel test1 = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1600686482/eib6rz0mzjlqw0rp1vfc.m3u8");
        MovieModel test3 = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1600686490/dzikdq9z8gfs6x40osyt.m3u8");


        adapter = new HomeFragmentRecyclerViewAdapter(getContext(), Arrays.asList(test, test1, test3), mainActivityContext);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);

        parcelable = linearLayoutManager.onSaveInstanceState();
        outState.putParcelable(PARCEABLE_LAYOUT_MANAGER_KEY, parcelable);
    }

    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null){
            parcelable = savedInstanceState.getParcelable(PARCEABLE_LAYOUT_MANAGER_KEY);
        }
    }

    private void initData(){
        if(parcelable != null){
            linearLayoutManager.onRestoreInstanceState(parcelable);
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("CARRR", "STOPEED");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CARRR", "PAUSED");
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
        initData();
    }
}