package com.echoeyecodes.sinnerman;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.echoeyecodes.sinnerman.Activities.ProfileActivity;
import com.echoeyecodes.sinnerman.Activities.SearchActivity;
import com.echoeyecodes.sinnerman.BottomNavigationFragments.ExploreFragment;
import com.echoeyecodes.sinnerman.BottomNavigationFragments.HomeFragment;
import com.echoeyecodes.sinnerman.BottomNavigationFragments.NotificationFragment;
import com.echoeyecodes.sinnerman.Activities.VideoActivity;
import com.echoeyecodes.sinnerman.Interface.MainActivityContext;
import com.echoeyecodes.sinnerman.Models.UserModel;
import com.echoeyecodes.sinnerman.Utils.AuthUserManager;
import com.echoeyecodes.sinnerman.Utils.AuthenticationManager;
import com.echoeyecodes.sinnerman.viewmodel.MainActivityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MainActivityContext, BottomNavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {
    private MainActivityViewModel mainActivityViewModel;
    private BottomNavigationView bottomNavigationView;
    private TextView search_btn;
    private CircleImageView circleImageView;
    private RootBottomFragment active_fragment;
    private ImageView user_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        AuthenticationManager authenticationManager = new AuthenticationManager();

        String token = authenticationManager.checkToken(this);
        if (token == null || token.equals("")) {
            authenticationManager.startAuthActivity(this);
        } else {
            beginActivity();
        }


        mainActivityViewModel.getIsLoaded().observe(this, (value) ->{
            if(value){
                initUserData();
            }
        });
    }


    public void initUserData() {
        if(!userExists()){
            mainActivityViewModel.updateCurrentUser();
            return;
        }
        UserModel userModel = AuthUserManager.getInstance().getUser(this);
        circleImageView = findViewById(R.id.user_profile_btn);


        if(userModel != null){
            Glide.with(this).load(Uri.parse(userModel.getProfile_url())).into(circleImageView);
        }
    }

    public Boolean userExists(){
        AuthUserManager authUserManager = AuthUserManager.getInstance();
        return authUserManager.dataExists(this);
    }

    public void beginActivity() {
        setContentView(R.layout.activity_main);

        initUserData();
        initViews();
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        user_profile = findViewById(R.id.user_profile_btn);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        search_btn = findViewById(R.id.search_input_button);

        search_btn.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchActivity.class));
        });

        user_profile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        getSupportFragmentManager().addOnBackStackChangedListener(this);

        navigateToBottomFragment(HomeFragment.Companion.newInstance());
    }


    private void navigateToBottomFragment(RootBottomFragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment existingFragment = getSupportFragmentManager().findFragmentByTag(fragment.getTAG());
        if (existingFragment != null) {
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, existingFragment, fragment.getTAG());
        } else {
            fragmentTransaction.replace(R.id.bottom_navigation_fragment_container, fragment, fragment.getTAG());
        }

        if (active_fragment == null || !fragment.getTAG().equals(active_fragment.getTAG())) {
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
        intent.putExtra("video_id", video_url);
        startActivity(intent);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        RootBottomFragment fragment;
        switch (item.getItemId()) {
            case R.id.action_home:
                fragment = HomeFragment.Companion.newInstance();
                openFragment(fragment, fragment.getTAG());
                return true;
            case R.id.action_notifications:
                fragment = NotificationFragment.Companion.newInstance();
                openFragment(fragment, fragment.getTAG());
                return true;
            case R.id.action_explore:
                fragment = ExploreFragment.Companion.newInstance();
                openFragment(fragment, fragment.getTAG());
                return true;
        }
        return false;

    }

    @Override
    public void onBackStackChanged() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        Fragment fragment = fragments.get(fragments.size() - 1);
        if (fragment instanceof RootBottomFragment) {
            active_fragment = (RootBottomFragment) fragment;
        }
    }
}