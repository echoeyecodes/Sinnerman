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
    private Fragment active_fragment;

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

        active_fragment = HomeFragment.newInstance();
        navigateToBottomFragment(active_fragment, HomeFragment.TAG);
    }

    private void navigateToBottomFragment(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (existingFragment != null) {
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, existingFragment, tag);
        } else {
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, fragment, tag);
        }
        fragmentTransaction.addToBackStack(null);
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
        navigateToBottomFragment(fragment, tag);
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
        switch (item.getItemId()) {
            case R.id.action_home:
                openFragment(HomeFragment.newInstance(), HomeFragment.TAG);
                return true;
            case R.id.action_notifications:
                openFragment(LibraryFragment.newInstance(), LibraryFragment.TAG);
                return true;
            case R.id.action_explore:
                openFragment(ExploreFragment.newInstance(), ExploreFragment.TAG);
                return true;
        }
        return false;

    }
}