package com.example.myapplication;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.Activities.LoginActivity;
import com.example.myapplication.BottomNavigationFragments.HomeFragment;
import com.example.myapplication.BottomNavigationFragments.LibraryFragment;
import com.example.myapplication.Activities.VideoActivity;
import com.example.myapplication.Fragments.YourVideosFragment;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.ViewModel.MainActivityViewModel;
import com.example.myapplication.util.BottomNavigationHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements MainActivityContext, BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationHandler bottomNavigationHandler;
    private MainActivityViewModel mainActivityViewModel;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        bottomNavigationHandler = new ViewModelProvider(this).get(BottomNavigationHandler.class);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        bottomNavigationHandler.getFragmentLiveData().observe(this, this::bottomHandler);

    }

    private void bottomHandler(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, fragment);
        fragmentTransaction.commit();
        setBottomViewTint(fragment);
    }

    private void removeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }

    public void setBottomViewTint(Fragment fragment){
        if(fragment instanceof HomeFragment){
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }else if (fragment instanceof LibraryFragment){
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        showSystemUI();

        startActivity(new Intent(this, LoginActivity.class));

    }

    private void showSystemUI(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    public void onBackPressed() {
        if(bottomNavigationHandler.getFragmentSize() > 1){
           Fragment fragment = bottomNavigationHandler.popBackStack();
           removeFragment(fragment);
            return;
        }
        super.onBackPressed();
    }

    public void navigateToFragment(Fragment fragment) {
        bottomNavigationHandler.addFragment(fragment);
    }

    @Override
    public void openFragment(Fragment fragment) {
        navigateToFragment(fragment);
    }

    @Override
    public void openYourVideosFragment() {
        navigateToFragment(YourVideosFragment.newInstance());
    }

    @Override
    public void navigateToVideos(String video_url) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("video_url", video_url);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.action_home:
                fragment = new HomeFragment();
                openFragment(fragment);
                return true;
            case R.id.action_notifications:
                fragment = new LibraryFragment();
                openFragment(fragment);
                return true;
        }
        return false;
    }
}