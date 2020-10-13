package com.example.myapplication.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.myapplication.Activities.AuthActivities.SignUpActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Models.OtpModel;
import com.example.myapplication.R;
import com.google.gson.Gson;

public class AuthenticationManager {

    public void saveToken(Context context, String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preference_key_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_token", token);
        new Thread(() -> {
            editor.commit();
            startMainActivity(context);
        }).start();
    }

    private void startMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public String tokenExtractor(String body){
        Gson gson = new Gson();
        OtpModel otpModel = gson.fromJson(body, OtpModel.class);
        return otpModel.getToken();
    }

    public void startAuthActivity(Context context){
        Intent intent = new Intent(context, SignUpActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public String checkToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preference_key_file), Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_token", null);
    }
}
