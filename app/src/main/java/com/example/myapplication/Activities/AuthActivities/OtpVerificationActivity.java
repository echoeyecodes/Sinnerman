package com.example.myapplication.Activities.AuthActivities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.API.ApiUtils.RequestStatus;
import com.example.myapplication.CustomView.FormEditText;
import com.example.myapplication.CustomView.FormEditTextListener;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AuthenticationManager;
import com.example.myapplication.ViewModel.AuthViewModel.OtpViewModel;
import com.google.android.material.button.MaterialButton;

public class OtpVerificationActivity extends AppCompatActivity implements FormEditTextListener {

    private MaterialButton change_to_sign_up_btn;
    private OtpViewModel otpViewModel;
    private MaterialButton otp_verify_btn;
    private FormEditText otp_field;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp_field = findViewById(R.id.otp_input_field);
        otpViewModel = new ViewModelProvider(this).get(OtpViewModel.class);

        otpViewModel.getErrorObserver().observe(this, (value) -> {
            otp_field.setError(true, value.getMessage());
        });


        String verification_response = getIntent().getStringExtra("verification_response");
        otpViewModel.setVerification_response(verification_response);

        Toast.makeText(this, verification_response, Toast.LENGTH_LONG).show();
        otp_verify_btn = findViewById(R.id.otp_verify_button);

        otpViewModel.getRequestStatusObserver().observe(this, (value) -> {
            otp_verify_btn.setEnabled(value != RequestStatus.LOADING);

            if (value == RequestStatus.ERROR) {
                //display error to user where applicable
                Toast.makeText(this, otpViewModel.getMessage(), Toast.LENGTH_SHORT).show();

            } else if (value == RequestStatus.SUCCESS) {
                new AuthenticationManager().saveToken(this, otpViewModel.getToken());
            }

        });

        otp_verify_btn.setOnClickListener(v ->{otpViewModel.submitForm();});
    }

    @Override
    public void onTextInput(String type, String value) {
        if (otpViewModel != null) {
            otpViewModel.setForm_field(type, value);
            otpViewModel.validateSingleField(type);
        }
    }

    @Override
    public void onFocused(String key, boolean isFocused) {
        if (!isFocused) {
            otpViewModel.validateSingleField(key);
        }
    }
}
