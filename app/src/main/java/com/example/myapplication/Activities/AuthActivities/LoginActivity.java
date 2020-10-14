package com.example.myapplication.Activities.AuthActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.API.DAO.RequestStatus;
import com.example.myapplication.CustomView.FormEditText;
import com.example.myapplication.CustomView.FormEditTextListener;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Utils.AuthenticationManager;
import com.example.myapplication.ViewModel.AuthViewModel.LoginViewModel;
import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity implements FormEditTextListener {

    private LoginViewModel loginViewModel;
    private MaterialButton change_to_sign_up_btn;
    private MaterialButton sign_in_btn;
    private FormEditText sign_in_username;
    private FormEditText sign_in_password;
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void onResume() {
        super.onResume();

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        change_to_sign_up_btn = findViewById(R.id.change_to_sign_up_btn);
        sign_in_btn = findViewById(R.id.sign_in_btn);
        sign_in_username = findViewById(R.id.sign_in_username);
        sign_in_password = findViewById(R.id.sign_in_password);

        loginViewModel.getErrorObserver().observe(this, (value) -> {
            getView(value.getKey()).setError(true, value.getMessage());
        });

        change_to_sign_up_btn.setOnClickListener( v ->{
            new AuthenticationManager().startAuthActivity(this);
        });

        loginViewModel.getRequestStatusObserver().observe(this, (value) ->{
            sign_in_btn.setEnabled(value != RequestStatus.LOADING);

            if(value == RequestStatus.NOT_VERIFIED){
                Intent intent = new Intent(this, OtpVerificationActivity.class);
                intent.putExtra("verification_response", loginViewModel.getVerification_response());
                startActivity(intent);
                loginViewModel.setRequestStatus(RequestStatus.NONE);
            }else if(value == RequestStatus.ERROR){
                Toast.makeText(this, loginViewModel.getMessage(), Toast.LENGTH_SHORT).show();

            }else if(value == RequestStatus.SUCCESS){
                    new AuthenticationManager().saveToken(this, loginViewModel.getToken());
            }
        });

        sign_in_btn.setOnClickListener(v -> loginViewModel.submitForm());

    }

    public FormEditText getView(String key) {
        switch (key) {
            case "username":
                return sign_in_username;
            case "password":
                return sign_in_password;
            default:
                return null;
        }
    }

    @Override
    public void onTextInput(String type, String value) {
        if (loginViewModel != null) {
            loginViewModel.setForm_field(type, value);
            loginViewModel.validateSingleField(type);
        }
    }

    @Override
    public void onFocused(String key, boolean isFocused) {
        if (!isFocused) {
            loginViewModel.validateSingleField(key);
        }
    }
}
