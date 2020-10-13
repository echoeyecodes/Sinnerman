package com.example.myapplication.Activities.AuthActivities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.myapplication.API.DAO.RequestStatus;
import com.example.myapplication.CustomView.FormEditText;
import com.example.myapplication.CustomView.FormEditTextListener;
import com.example.myapplication.R;
import com.example.myapplication.ViewModel.AuthViewModel.SignUpViewModel;
import com.google.android.material.button.MaterialButton;

public class SignUpActivity extends AppCompatActivity implements FormEditTextListener {
    private SignUpViewModel signUpViewModel;
    private MaterialButton sign_up_btn;
    private MaterialButton have_account_btn;
    private FormEditText first_name;
    private FormEditText last_name;
    private FormEditText password;
    private FormEditText email;
    private FormEditText username;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        username = findViewById(R.id.edit_text_username);
        first_name = findViewById(R.id.edit_text_first_name);
        last_name = findViewById(R.id.edit_text_lastname);
        email = findViewById(R.id.edit_text_email);
        password = findViewById(R.id.edit_text_password);
        have_account_btn = findViewById(R.id.have_account_btn);

        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        signUpViewModel.getErrorObserver().observe(this, (value) -> {
            getView(value.getKey()).setError(true, value.getMessage());
        });

        signUpViewModel.getRequestStatusObserver().observe(this, (value) ->{
            sign_up_btn.setEnabled(value != RequestStatus.LOADING);

            if(value == RequestStatus.SUCCESS){
                Intent intent = new Intent(this, OtpVerificationActivity.class);
                intent.putExtra("email", signUpViewModel.getForm_fields().get("email"));
                startActivity(intent);
                signUpViewModel.setRequestStatus(RequestStatus.NONE);
            }else if (value == RequestStatus.EXISTS){
                Toast.makeText(this, "Account exists", Toast.LENGTH_SHORT).show();
            }else if(value == RequestStatus.ERROR){
                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        sign_up_btn = findViewById(R.id.sign_up_btn);

        sign_up_btn.setOnClickListener(v -> {
            signUpViewModel.submitForm();
        });

        have_account_btn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onTextInput(String type, String value) {
        if (signUpViewModel != null) {
            signUpViewModel.setForm_field(type, value);
            signUpViewModel.validateSingleField(type);
        }
    }

    public FormEditText getView(String key) {
        switch (key) {
            case "firstname":
                return first_name;
            case "lastname":
                return last_name;
            case "email":
                return email;
            case "password":
                return password;
            case "username":
                return username;
            default:
                return null;
        }
    }

    @Override
    public void onFocused(String key, boolean isFocused) {
        if (!isFocused) {
            signUpViewModel.validateSingleField(key);
        }
    }
}
