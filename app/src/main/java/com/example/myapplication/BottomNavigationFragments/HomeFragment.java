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

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.*;
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Models.MovieModel;
import com.example.myapplication.R;
import com.example.myapplication.Utils.VideosItemCallback;
import com.example.myapplication.ViewModel.MainActivityViewModel;
import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment {

    private static HomeFragment homeFragment;
    private RecyclerView recyclerView;
    private HomeFragmentRecyclerViewAdapter adapter;
    private MainActivityContext mainActivityContext;
    private MainActivityViewModel viewModel;
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
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        final VideosItemCallback videosItemCallback = VideosItemCallback.newInstance();

        adapter = new HomeFragmentRecyclerViewAdapter(videosItemCallback, getContext(), mainActivityContext);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        viewModel.getVideos().observe(getViewLifecycleOwner(), videos ->{
            adapter.submitList(videos);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter = null;
    }


    @Override
    public void onSaveInstanceState(@NonNull @NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        parcelable = linearLayoutManager.onSaveInstanceState();
        outState.putParcelable(PARCEABLE_LAYOUT_MANAGER_KEY, parcelable);
        outState.putSerializable("TEST", adapter);
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            adapter = (HomeFragmentRecyclerViewAdapter) savedInstanceState.getSerializable("TEST");
        }
    }

    private void initData(){
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
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
}