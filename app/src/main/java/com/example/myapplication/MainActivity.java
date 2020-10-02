package com.example.myapplication;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.BottomNavigationFragments.LibraryFragment;
import com.example.myapplication.BottomNavigationFragments.HomeFragment;
import com.example.myapplication.Fragments.CommentsDialogFragment;
import com.example.myapplication.Fragments.VideoFragment;
import com.example.myapplication.Fragments.YourVideosFragment;
import com.example.myapplication.Interface.MainActivityContext;
import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.ViewModel.MainActivityViewModel;
import com.example.myapplication.util.BottomNavigationHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainActivityContext {

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationHandler bottomNavigationHandler;
    private MainActivityViewModel mainActivityViewModel;
    private LinearLayout toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        toolbar = findViewById(R.id.home_tool_bar);
        bottomNavigationHandler = new ViewModelProvider(this).get(BottomNavigationHandler.class);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        bottomNavigationHandler.getFragmentLiveData().observe(this, fragment ->{
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, fragment);
            fragmentTransaction.commit();
            setBottomViewTint(fragment);
            handleSystemUI(fragment);
        });

        mainActivityViewModel.getIsFullScreen().observe(this, (isFullScreen) ->{
            if(isFullScreen){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }else{
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void setBottomViewTint(Fragment fragment){
        if(fragment instanceof HomeFragment){
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            mainActivityViewModel.toggleFullScreen(false);
        }else if (fragment instanceof LibraryFragment){
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }
    }

    private void handleSystemUI(Fragment fragment){
        if(fragment instanceof VideoFragment){
            hideSystemUI();
            hideViews();
        }else{
            showSystemUI();
            showViews();
        }
    }

    private void hideViews(){
        bottomNavigationView.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
    }

    private void showViews(){
        if(bottomNavigationView.getVisibility() == View.GONE || toolbar.getVisibility() == View.GONE){
            bottomNavigationView.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
        }
    }

    private void hideSystemUI(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }


    private void showSystemUI(){
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(bottomNavigationHandler.getCurrentFragment() instanceof VideoFragment){
            hideSystemUI();
        }else{
            showSystemUI();
        }
    }

    @Override
    public void onBackPressed() {
        if(bottomNavigationHandler.getFragmentSize() > 1){
            bottomNavigationHandler.popBackStack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId()) {
            case R.id.action_home:
                    fragment = new HomeFragment();
                    navigateToFragment(fragment);
                return true;
            case R.id.action_notifications:
                    fragment = new LibraryFragment();
                    navigateToFragment(fragment);
                return true;
        }
        return false;
    }

    private void navigateToFragment(Fragment fragment) {
        bottomNavigationHandler.addFragment(fragment);
    }

    @Override
    public void openComments() {
        CommentsDialogFragment.newInstance().show(getSupportFragmentManager(), "comments_dialog");
    }

    @Override
    public void openVideoFragment(String video_url) {
        Fragment fragment = new VideoFragment(video_url);
        navigateToFragment(fragment);
    }

    @Override
    public void openYourVideosFragment() {
        navigateToFragment(YourVideosFragment.newInstance());
    }

    @Override
    public void toggleFullScreen(boolean value) {
        mainActivityViewModel.toggleFullScreen(value);
    }

    @Override
    public void likeVideo(VideoModel videoModel) {
        mainActivityViewModel.likeVideo(videoModel);
    }
}