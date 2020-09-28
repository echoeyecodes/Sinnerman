package com.example.myapplication.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter;
import com.example.myapplication.BottomNavigationFragments.HomeFragment;
import com.example.myapplication.Factory.Factories.HomeVideoAdapterFactory;
import com.example.myapplication.Factory.FactoryGenerator;
import com.example.myapplication.Factory.TYPE;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Models.MovieModel;
import com.example.myapplication.R;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class VideoFragment extends Fragment {
    private RecyclerView recyclerView;
    private HomeFragmentRecyclerViewAdapter adapter;
    private static VideoFragment videoFragment;
    private MainActivityContext mainActivityContext;
    private static final String PARCEABLE_LAYOUT_MANAGER_KEY = "PARCEABLE_LAYOUT_MANAGER_KEY1";
    LinearLayoutManager linearLayoutManager;
    Parcelable parcelable;

    public static VideoFragment newInstance(){
        if(videoFragment == null){
            videoFragment = new VideoFragment();
        }
        return videoFragment;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.fragment_video_recycler_view);
        HomeVideoAdapterFactory homeVideoAdapterFactory = (HomeVideoAdapterFactory) FactoryGenerator.getFactory(TYPE.VIDEOS);

        MovieModel test = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1600686480/xf6knnaxf6yca9lzgz7d.m3u8");
        MovieModel test1 = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1600686482/eib6rz0mzjlqw0rp1vfc.m3u8");
        MovieModel test3 = new MovieModel("123456", "The beginning", "https://res.cloudinary.com/echoeyecodes/video/upload/v1600686490/dzikdq9z8gfs6x40osyt.m3u8");

        assert homeVideoAdapterFactory != null;
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        adapter = homeVideoAdapterFactory.getHomeFragmentAdapter(getContext(), Arrays.asList(test, test1, test3), mainActivityContext);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

        initData();
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
    public void onAttach(@NonNull @NotNull Context context) {
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
}
