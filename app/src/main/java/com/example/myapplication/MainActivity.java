package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.myapplication.BottomNavigationFragments.ExploreFragment;
import com.example.myapplication.BottomNavigationFragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private static int active_bottom_fragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        navigateToFragment(HomeFragment.newInstance(), "HOME_FRAGMENT");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home:
                if(active_bottom_fragment != 0){
                    navigateToFragment(HomeFragment.newInstance(), "HOME_FRAGMENT");
                }
                active_bottom_fragment = 0;
                return true;
            case R.id.action_search:
                if(active_bottom_fragment != 1){
                    navigateToFragment(ExploreFragment.newInstance(), "EXPLORE_FRAGMENT");
                }
                active_bottom_fragment = 1;
                return true;
        }
        return false;
    }

    private void navigateToFragment(Fragment fragment, String TAG){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if(existingFragment != null){
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, existingFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return;
        }
        fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, fragment, TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if(active_bottom_fragment != 0){
            navigateToFragment(HomeFragment.newInstance(), "HOME_FRAGMENT");
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
            active_bottom_fragment = 0;
            return;
        }
        finish();
    }
}