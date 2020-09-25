package com.example.myapplication;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.myapplication.BottomNavigationFragments.ExploreFragment;
import com.example.myapplication.BottomNavigationFragments.HomeFragment;
import com.example.myapplication.Fragments.CommentsDialogFragment;
import com.example.myapplication.Interface.ToggleFullScreen;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener, ToggleFullScreen{

    private BottomNavigationView bottomNavigationView;
    private static int active_bottom_fragment = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        getSupportFragmentManager().addOnBackStackChangedListener(this);
        navigateToFragment(HomeFragment.newInstance(), "HOME_FRAGMENT");
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
                    navigateToFragment(HomeFragment.newInstance(), "HOME_FRAGMENT");
                active_bottom_fragment = 0;
                return true;
            case R.id.action_search:
                    navigateToFragment(ExploreFragment.newInstance(), "EXPLORE_FRAGMENT");
                active_bottom_fragment = 1;
                return true;
        }
        return false;
    }

    private void navigateToFragment(Fragment fragment, String TAG) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if(existingFragment != null){
            fragmentTransaction.show(existingFragment);
            fragmentTransaction.commit();
            return;
        }
        fragmentTransaction.add(R.id.bottom_navigation_fragment_container, fragment, TAG);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.commit();
    }


//    @Override
//    public void onBackPressed() {
//        if (active_bottom_fragment != 0) {
//            navigateToFragment(HomeFragment.newInstance(), "HOME_FRAGMENT");
//            bottomNavigationView.getMenu().getItem(0).setChecked(true);
//            active_bottom_fragment = 0;
//            return;
//        }
//        finish();
//    }

    @Override
    public void openComments() {
        CommentsDialogFragment.newInstance().show(getSupportFragmentManager(), "comments_dialog");
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            Log.d("CARRR", String.valueOf(fragment.getTag()));
        }
        FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);


    }
}