package com.example.myapplication.BottomNavigationFragments;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;
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
import com.example.myapplication.R;
import com.example.myapplication.RootBottomFragment;
import com.example.myapplication.Utils.CustomItemDecoration;
import com.example.myapplication.Utils.IntegerToDp;
import com.example.myapplication.Utils.VideosItemCallback;
import com.example.myapplication.ViewModel.MainActivityViewModel;
import com.google.android.material.chip.Chip;
import org.jetbrains.annotations.NotNull;

public class HomeFragment extends RootBottomFragment {
    private RecyclerView recyclerView;
    private RecyclerView chipsRecyclerView;
    public static final String TAG = "HOME_FRAGMENT";
    private HomeFragmentRecyclerViewAdapter adapter;
    private static HomeFragment homeFragment;
    private MainActivityViewModel viewModel;
    LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager chipsLayoutManager;
    private static final String[] items = {"Recent", "Most Liked", "Most Comment", "Top Views"};

    public HomeFragment(){

    }

    public static HomeFragment newInstance() {
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
    }

    @Override
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.fragment_home_recycler_view);
        chipsRecyclerView = view.findViewById(R.id.video_filters);
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        final VideosItemCallback videosItemCallback = VideosItemCallback.newInstance();

        adapter = new HomeFragmentRecyclerViewAdapter(videosItemCallback, getContext(), mainActivityContext);
        ChipsAdapter chipsAdapter = new ChipsAdapter(items);
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        chipsLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        chipsRecyclerView.setLayoutManager(chipsLayoutManager);
        recyclerView.setLayoutManager(linearLayoutManager);

        chipsRecyclerView.addItemDecoration(new CustomItemDecoration(0, IntegerToDp.intToDp(5)));
        recyclerView.addItemDecoration(new CustomItemDecoration(IntegerToDp.intToDp(15), IntegerToDp.intToDp(15)));
        chipsRecyclerView.setAdapter(chipsAdapter);
        recyclerView.setAdapter(adapter);
        chipsAdapter.notifyDataSetChanged();

        viewModel.getVideos().observe(getViewLifecycleOwner(), videos ->{
            adapter.submitList(videos);
        });

    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        mainActivityContext.setActiveBottomViewFragment(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        homeFragment = null;
    }

    private static class ChipsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private String[] items;

        public ChipsAdapter(String[] items){
            this.items = items;
        }

        @NonNull
        @NotNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_home_chips_item, parent, false);
            return new ChipsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
            ((ChipsViewHolder) holder).chip.setText(items[position]);
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        private static class ChipsViewHolder extends RecyclerView.ViewHolder{
            Chip chip;
            public ChipsViewHolder(@NonNull @NotNull View itemView) {
                super(itemView);

                chip = itemView.findViewById(R.id.home_chips_item);
            }
        }
    }
}