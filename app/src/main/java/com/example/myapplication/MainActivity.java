package com.example.myapplication;

import android.view.View;
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
import com.example.myapplication.Models.MovieModel;
import com.example.myapplication.ViewModel.MainActivityViewModel;
import com.example.myapplication.util.BottomNavigationHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, MainActivityContext {

    private BottomNavigationView bottomNavigationView;
    private BottomNavigationHandler bottomNavigationHandler;
    private MainActivityViewModel mainActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationHandler = new ViewModelProvider(this).get(BottomNavigationHandler.class);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        bottomNavigationHandler.getFragmentLiveData().observe(this, fragment ->{
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, fragment);
            fragmentTransaction.commit();
            setBottomViewTint(fragment);
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        navigateToFragment(HomeFragment.newInstance());
    }

    private void setBottomViewTint(Fragment fragment){
        if(fragment instanceof HomeFragment){
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }else if (fragment instanceof LibraryFragment){
            bottomNavigationView.getMenu().getItem(1).setChecked(true);
        }

        if(fragment instanceof VideoFragment){
            hideSystemUI();
        }else{
            showSystemUI();
        }
    }

    private void hideSystemUI(){
        bottomNavigationView.setVisibility(View.GONE);
    }

    private void showSystemUI(){
        if(bottomNavigationView.getVisibility() == View.GONE){
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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
        switch (item.getItemId()) {
            case R.id.action_home:
                    navigateToFragment(HomeFragment.newInstance());
                return true;
            case R.id.action_library:
                    navigateToFragment(LibraryFragment.newInstance());
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
    public void openVideoFragment() {
        navigateToFragment(VideoFragment.newInstance());
    }

    @Override
    public void openYourVideosFragment() {
        navigateToFragment(YourVideosFragment.newInstance());
    }

    @Override
    public void likeVideo(MovieModel movieModel) {
        mainActivityViewModel.likeVideo(movieModel);
    }
}