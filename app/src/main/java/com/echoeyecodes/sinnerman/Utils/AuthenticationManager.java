package com.echoeyecodes.sinnerman.Utils;

import android.app.Activity;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.echoeyecodes.sinnerman.Activities.AuthActivities.SignUpActivity;
import com.echoeyecodes.sinnerman.MainActivity;
import com.echoeyecodes.sinnerman.Models.OtpModel;
import com.echoeyecodes.sinnerman.R;
import com.echoeyecodes.sinnerman.services.NotificationService;
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

    public void deleteToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preference_key_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("user_token");
        editor.commit();
    }

    public void startMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void signOut(Context context){
        deleteToken(context);
        Intent intent = new Intent(context, SignUpActivity.class);
        JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(0);
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
        Activity activity = (Activity) context;
        activity.finish();
        context.startActivity(intent);
    }

    public String checkToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.shared_preference_key_file), Context.MODE_PRIVATE);
        return sharedPreferences.getString("user_token", "");
    }
}
