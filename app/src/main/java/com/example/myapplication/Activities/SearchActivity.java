package com.example.myapplication.Activities;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.example.myapplication.Fragments.DefaultFragment;
import com.example.myapplication.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import org.jetbrains.annotations.NotNull;

public class SearchActivity extends AppCompatActivity {
private TabLayout tabLayout;
private ViewPager2 viewPager;
private static String[] tabs={"Videos", "Comments", "Accounts"};

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tabLayout = findViewById(R.id.search_tab_layout);
        viewPager = findViewById(R.id.search_view_pager);

        viewPager.setAdapter(new SearchFragmentAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) ->{
            tab.setText(tabs[position]);
        }).attach();
    }

    private static class SearchFragmentAdapter extends FragmentStateAdapter{


        public SearchFragmentAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @NotNull
        @Override
        public Fragment createFragment(int position) {
            return new DefaultFragment();
        }

        @Override
        public int getItemCount() {
            return tabs.length;
        }
    }
}
