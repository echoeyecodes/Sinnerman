package com.echoeyecodes.sinnerman.Activities.AuthActivities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.echoeyecodes.sinnerman.API.ApiUtils.RequestStatus;
import com.echoeyecodes.sinnerman.CustomView.FormEditText;
import com.echoeyecodes.sinnerman.CustomView.FormEditTextListener;
import com.echoeyecodes.sinnerman.R;
import com.echoeyecodes.sinnerman.Utils.AuthenticationManager;
import com.echoeyecodes.sinnerman.viewmodel.authViewModel.OtpViewModel;
import com.google.android.material.button.MaterialButton;

public class OtpVerificationActivity extends AppCompatActivity implements FormEditTextListener {

    private MaterialButton resend_otp_btn;
    private OtpViewModel otpViewModel;
    private MaterialButton otp_verify_btn;
    private FormEditText otp_field;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        otp_field = findViewById(R.id.otp_input_field);
        resend_otp_btn = findViewById(R.id.resend_otp_btn);
        otpViewModel = new ViewModelProvider(this).get(OtpViewModel.class);

        otpViewModel.getErrorObserver().observe(this, (value) -> {
            otp_field.setError(true, value.getMessage());
        });


        String verification_response = getIntent().getStringExtra("verification_response");
        assert verification_response != null;
        otpViewModel.setVerification_response(verification_response);

        otp_verify_btn = findViewById(R.id.otp_verify_button);

        otpViewModel.getRequestStatusObserver().observe(this, (value) -> {
            otp_verify_btn.setEnabled(value != RequestStatus.LOADING);
            resend_otp_btn.setEnabled(value != RequestStatus.LOADING);

            if (value == RequestStatus.ERROR) {
                //display error to user where applicable
                Toast.makeText(this, otpViewModel.getMessage(), Toast.LENGTH_SHORT).show();

            } else if (value == RequestStatus.SUCCESS) {
                new AuthenticationManager().saveToken(this, otpViewModel.getToken());
            }

        });

        otp_verify_btn.setOnClickListener(v ->{otpViewModel.submitForm();});

        resend_otp_btn.setOnClickListener(v -> otpViewModel.resendOtp());
    }

    @Override
    public void onTextInput(String type, String value) {
        if (otpViewModel != null) {
            otpViewModel.getForm_fields().put(type, value);
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
