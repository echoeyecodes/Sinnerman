package com.echoeyecodes.sinnerman;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.echoeyecodes.sinnerman.Fragments.ProgressDialogFragment;
import com.echoeyecodes.sinnerman.Interface.MainActivityContext;
import com.echoeyecodes.sinnerman.Models.UserModel;
import com.echoeyecodes.sinnerman.Models.VideoResponseBody;
import com.echoeyecodes.sinnerman.Room.CommentDatabase;
import com.echoeyecodes.sinnerman.Room.Dao.CommentDao;
import com.echoeyecodes.sinnerman.Utils.AuthUserManager;
import com.echoeyecodes.sinnerman.Utils.AuthenticationManager;
import com.echoeyecodes.sinnerman.repository.CommentRepository;
import com.echoeyecodes.sinnerman.viewmodel.MainActivityViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MainActivityContext, BottomNavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {
    private MainActivityViewModel mainActivityViewModel;
    private BottomNavigationView bottomNavigationView;
    private TextView search_btn;
    private CircleImageView circleImageView;
    private RootBottomFragment active_fragment;
    private ImageView user_profile;
    private FrameLayout frameLayout;

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


        mainActivityViewModel.getIsLoaded().observe(this, (value) -> {
            if (value) {
                initUserData();
            }
        });
    }


    public void initUserData() {
        UserModel userModel = AuthUserManager.getInstance().getUser(this);
        circleImageView = findViewById(R.id.user_profile_btn);
        if (userModel != null) {
            Glide.with(this).load(Uri.parse(userModel.getProfile_url())).into(circleImageView);
        }
    }

    public void refreshUserData() {
        mainActivityViewModel.updateCurrentUser();
    }

    public void beginActivity() {
        setContentView(R.layout.activity_main);

        initUserData();
        refreshUserData();
        initViews();
    }

    private void initViews() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        user_profile = findViewById(R.id.user_profile_btn);
        frameLayout = findViewById(R.id.main_activity_root);

        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        search_btn = findViewById(R.id.search_input_button);

        user_profile.setOnClickListener(v -> {
            startActivityForResult(new Intent(this, ProfileActivity.class), 0);
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
    public void onOptionSelected(VideoResponseBody video, int position) {

        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment("Creating Link");
        progressDialogFragment.setCancelable(false);
        progressDialogFragment.show(getSupportFragmentManager(), "link_sync_fragment");

        String title = video.getVideo().getTitle();
        String description = video.getVideo().getDescription();
        String imageUrl = video.getVideo().getThumbnail();

        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://www.sinnerman.com/videos/" + video.getVideo().getId()))
                .setDomainUriPrefix("https://sinnerman.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().setFallbackUrl(Uri.parse(BuildConfig.APP_REDIRECT_URL))
                        .build()).setSocialMetaTagParameters(new DynamicLink.SocialMetaTagParameters.Builder()
                .setTitle(title).setDescription(description).setImageUrl(Uri.parse(imageUrl)).build())
                .buildShortDynamicLink().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                String link = Objects.requireNonNull(task.getResult().getShortLink()).toString();

                if(position == 0){
                    copyLinkToClipboard(link);
                }else if (position == 1){
                    openShareIntent(link);
                }
            }else{
                progressDialogFragment.dismiss();
                Toast.makeText(this, "Could not create link", Toast.LENGTH_SHORT).show();
            }
            progressDialogFragment.dismiss();
        }).addOnFailureListener(e -> {
            progressDialogFragment.dismiss();
            Toast.makeText(this, "Could not create link", Toast.LENGTH_SHORT).show();
        });
    }

    private void copyLinkToClipboard(String link){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Share link", link);
        clipboardManager.setPrimaryClip(clipData);
        showSnackBarMessage();
    }

    private void showSnackBarMessage(){
        Snackbar.make(frameLayout, "Video link copied to clipboard", Snackbar.LENGTH_LONG).show();
    }

    private void openShareIntent(String link){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, link);
        intent.setType("text/plain");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            refreshUserData();
        }
    }
}