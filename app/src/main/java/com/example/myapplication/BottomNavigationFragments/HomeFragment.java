package com.example.myapplication.BottomNavigationFragments;

import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.*;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.myapplication.Adapters.HomeFragmentRecyclerViewAdapter;
import com.example.myapplication.R;
import com.example.myapplication.RootBottomFragment;
import com.example.myapplication.Utils.CustomItemDecoration;
import com.example.myapplication.Utils.IntegerToDp;
import com.example.myapplication.Utils.VideosItemCallback;
import com.example.myapplication.ViewModel.BottomFragmentViewModel.HomeFragmentViewModel;
import com.example.myapplication.ViewModel.NetworkState;
import com.google.android.material.chip.Chip;
import org.jetbrains.annotations.NotNull;

public class HomeFragment extends RootBottomFragment implements SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView recyclerView;
    private RecyclerView chipsRecyclerView;
    private RelativeLayout loading_screen;
    private HomeFragmentRecyclerViewAdapter adapter;
    private static HomeFragment homeFragment;
    private HomeFragmentViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    private LinearLayoutManager chipsLayoutManager;
    private static final String[] items = {"Recent", "Most Liked", "Most Comment", "Top Views"};

    public HomeFragment(){
        TAG = "HOME_FRAGMENT";
    }

    public static HomeFragment newInstance() {
        if(homeFragment == null){
            homeFragment = new HomeFragment();
        }
        return homeFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireActivity()).get(HomeFragmentViewModel.class);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout = view.findViewById(R.id.home_swipe_refresh);
        loading_screen = view.findViewById(R.id.home_fragment_loading_layout);
        recyclerView = view.findViewById(R.id.fragment_home_recycler_view);
        chipsRecyclerView = view.findViewById(R.id.video_filters);

        swipeRefreshLayout.setOnRefreshListener(this);

        viewModel.getRequest_status().observe(requireActivity(), (value) ->{
            loading_screen.setVisibility(value == NetworkState.LOADING ? View.VISIBLE : View.GONE);

            if(value == NetworkState.ERROR){
                Toast.makeText(getContext(), viewModel.getMessage(), Toast.LENGTH_SHORT).show();
                viewModel.resetRequestStatus();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

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

        viewModel.getVideosObserver().observe(getViewLifecycleOwner(), videos ->{
            adapter.submitList(videos);
            swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onRefresh() {
        viewModel.fetchVideos();
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