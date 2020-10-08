package com.example.myapplication.Activities;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.Fragments.OtpDialogFragment;
import com.example.myapplication.R;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    @Override
    protected void onResume() {
        super.onResume();
        OtpDialogFragment otpDialogFragment = new OtpDialogFragment();
        otpDialogFragment.show(getSupportFragmentManager(), "OTP_DIALOG_FRAGMENT");

    }

}
