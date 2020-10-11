package com.example.myapplication.Activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adapters.ProfileAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Utils.CustomItemDecoration;
import com.example.myapplication.Utils.IntegerToDp;

public class ProfileActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private TextView title;
    private ImageButton back_btn;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        recyclerView = findViewById(R.id.profile_recycler_view);
        linearLayout = findViewById(R.id.profile_toolbar);

        title = linearLayout.findViewById(R.id.sub_activity_title);
        back_btn = linearLayout.findViewById(R.id.sub_activity_back_btn);

        title.setText("Profile");
        back_btn.setOnClickListener(v -> super.onBackPressed());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        ProfileAdapter profileAdapter = new ProfileAdapter(this);
        CustomItemDecoration customItemDecoration = new CustomItemDecoration(IntegerToDp.intToDp(10));
        recyclerView.addItemDecoration(customItemDecoration);
        recyclerView.setAdapter(profileAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
