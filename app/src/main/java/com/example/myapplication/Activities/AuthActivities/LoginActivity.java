package com.example.myapplication.Activities.AuthActivities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton change_to_sign_up_btn;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();

        change_to_sign_up_btn = findViewById(R.id.change_to_sign_up_btn);

        change_to_sign_up_btn.setOnClickListener( v ->{
            startActivity(new Intent(this, SignUpActivity.class));
        });

    }

}
