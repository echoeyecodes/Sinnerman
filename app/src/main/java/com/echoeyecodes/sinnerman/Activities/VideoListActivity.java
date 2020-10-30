package com.echoeyecodes.sinnerman.Activities;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.echoeyecodes.sinnerman.R;

public class VideoListActivity extends AppCompatActivity {
private LinearLayout toolbar;
private TextView toolbar_text;
private ImageButton toolbar_back_btn;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        toolbar = findViewById(R.id.video_list_toolbar);
        toolbar_text = toolbar.findViewById(R.id.sub_activity_title);
        toolbar_back_btn = toolbar.findViewById(R.id.sub_activity_back_btn);
        String title = getIntent().getStringExtra("title");
        toolbar_text.setText(title);

        toolbar_back_btn.setOnClickListener(v -> super.onBackPressed());
    }
}
