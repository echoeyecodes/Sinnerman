package com.example.myapplication;

import android.os.PersistableBundle;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.BottomNavigationFragments.ExploreFragment;
import com.example.myapplication.BottomNavigationFragments.HomeFragment;
import com.example.myapplication.Fragments.CommentsDialogFragment;
import com.example.myapplication.Interface.ToggleFullScreen;
import com.example.myapplication.ViewModel.MainActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import static android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ToggleFullScreen, View.OnSystemUiVisibilityChangeListener {

    private BottomNavigationView bottomNavigationView;
    private static int active_bottom_fragment = -1;
    View view;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        view = getWindow().getDecorView();
        view.setOnSystemUiVisibilityChangeListener(this);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        navigateToFragment(HomeFragment.newInstance(), "HOME_FRAGMENT");

        viewModel.getIsFullScreen().observe(this, (value) -> {
            if (value) {
                hideSystemUI();
            } else {
                showSystemUI();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                if (active_bottom_fragment != 0) {
                    navigateToFragment(HomeFragment.newInstance(), "HOME_FRAGMENT");
                }
                active_bottom_fragment = 0;
                return true;
            case R.id.action_search:
                if (active_bottom_fragment != 1) {
                    navigateToFragment(ExploreFragment.newInstance(), "EXPLORE_FRAGMENT");
                }
                active_bottom_fragment = 1;
                return true;
        }
        return false;
    }

    private void navigateToFragment(Fragment fragment, String TAG) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if (existingFragment != null) {
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, existingFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return;
        }
        fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, fragment, TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    private void hideUI() {
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void showUI() {
        bottomNavigationView.setVisibility(View.VISIBLE);
    }

    private void hideSystemUI() {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        hideUI();
    }

    private void showSystemUI() {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        showUI();
    }

    @Override
    public void onBackPressed() {
        if (viewModel.getIsFullScreen().getValue() != null && viewModel.getIsFullScreen().getValue()) {
            viewModel.setIsFullScreen(false);
            return;
        }
        if (active_bottom_fragment != 0) {
            navigateToFragment(HomeFragment.newInstance(), "HOME_FRAGMENT");
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            active_bottom_fragment = 0;
            return;
        }
        finish();
    }

    @Override
    public void toggleFullScreen(boolean value) {
        viewModel.setIsFullScreen(value);
    }

    @Override
    public void openComments() {
        CommentsDialogFragment.newInstance().show(getSupportFragmentManager(), "comments_dialog");
    }

    @Override
    public void onSystemUiVisibilityChange(int i) {
        if ((i & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
            showUI();
        } else {
            hideUI();
        }
    }
}