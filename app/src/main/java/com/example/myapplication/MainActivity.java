package com.example.myapplication;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.Activities.SearchActivity;
import com.example.myapplication.BottomNavigationFragments.ExploreFragment;
import com.example.myapplication.BottomNavigationFragments.HomeFragment;
import com.example.myapplication.BottomNavigationFragments.LibraryFragment;
import com.example.myapplication.Activities.VideoActivity;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.ViewModel.MainActivityViewModel;
import com.example.myapplication.util.BottomNavigationHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements MainActivityContext, BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationHandler bottomNavigationHandler;
    private MainActivityViewModel mainActivityViewModel;
    private BottomNavigationView bottomNavigationView;
    private TextView search_btn;
    private RootBottomFragment active_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationHandler = new ViewModelProvider(this).get(BottomNavigationHandler.class);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        search_btn = findViewById(R.id.search_input_button);

        search_btn.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
        });

        navigateToBottomFragment(HomeFragment.newInstance());
    }

    private void navigateToBottomFragment(RootBottomFragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(fragment.getTAG());
        if (existingFragment != null) {
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, existingFragment, fragment.getTAG());
        } else {
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, fragment, fragment.getTAG());
        }

        if(active_fragment == null || !fragment.getTAG().equals(active_fragment.getTAG())){
            fragmentTransaction.addToBackStack(null);
        }
        active_fragment = fragment;
        fragmentTransaction.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();
        showSystemUI();

    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void openFragment(Fragment fragment, String tag) {
        if (fragment instanceof RootBottomFragment) {
            navigateToBottomFragment((RootBottomFragment) fragment);
        }
    }

    @Override
    public void setActiveBottomViewFragment(int position) {
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void navigateToVideos(String video_url) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("video_url", video_url);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        RootBottomFragment fragment;
        switch (item.getItemId()) {
            case R.id.action_home:
                fragment = HomeFragment.newInstance();
                openFragment(fragment, fragment.getTAG());
                return true;
            case R.id.action_notifications:
                fragment = LibraryFragment.newInstance();
                openFragment(fragment, fragment.getTAG());
                return true;
            case R.id.action_explore:
                fragment = ExploreFragment.newInstance();
                openFragment(fragment, fragment.getTAG());
                return true;
        }
        return false;

    }
}