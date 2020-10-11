package com.example.myapplication.BottomNavigationFragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adapters.ExploreAdapter;
import com.example.myapplication.Adapters.ExploreItemAdapter;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.R;
import com.example.myapplication.RootBottomFragment;
import com.example.myapplication.Utils.CustomItemDecoration;
import com.example.myapplication.Utils.IntegerToDp;
import com.example.myapplication.ViewModel.MainActivityViewModel;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ExploreFragment extends RootBottomFragment {
    private RecyclerView recyclerView;
    private static ExploreFragment exploreFragment;
    private MainActivityViewModel mainActivityViewModel;
    public static final String TAG = "EXPLORE_FRAGMENT";
    private LinearLayoutManager linearLayoutManager;
    private ExploreAdapter exploreAdapter;

    public ExploreFragment() {
        // Required empty public constructor
    }

    public static ExploreFragment newInstance() {
        if(exploreFragment == null){
            exploreFragment = new ExploreFragment();
        }
        return exploreFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.explore_recycler_view);
        mainActivityViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        Map<String, List<VideoModel>> mock_data = new HashMap<>();
        mock_data.put("RECENTLY UPLOADED", mainActivityViewModel.fetchExploreVideos());
        mock_data.put("TRENDING", mainActivityViewModel.fetchExploreVideos());
        mock_data.put("TOP RATED", mainActivityViewModel.fetchExploreVideos());
        mock_data.put("TOP RATED 123", mainActivityViewModel.fetchExploreVideos());
        mock_data.put("TOP RATED 12345", mainActivityViewModel.fetchExploreVideos());
        mock_data.put("TOP RATED 1234wfdsgfds5", mainActivityViewModel.fetchExploreVideos());
        mock_data.put("TOP RATED 1234sdfdsfds5", mainActivityViewModel.fetchExploreVideos());
        mock_data.put("TOP RATED 123skldfsdfds45", mainActivityViewModel.fetchExploreVideos());

        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);
        exploreAdapter = new ExploreAdapter(mock_data, getContext(), mainActivityContext);
        recyclerView.addItemDecoration(new CustomItemDecoration(IntegerToDp.intToDp(10), IntegerToDp.intToDp(0)));
        recyclerView.setAdapter(exploreAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivityContext.setActiveBottomViewFragment(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        exploreFragment = null;
    }
}