package com.example.myapplication.Utils;

import android.content.Context;
import android.util.Log;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.viewmodel.MainActivityViewModel;

import java.io.*;

public class AuthUserManager {
    private static AuthUserManager authUserManager;
    private AuthUserManager(){

    }

    public static AuthUserManager getInstance(){
        if(authUserManager == null){
            authUserManager = new AuthUserManager();
        }
        return authUserManager;
    }

    public void saveUser(MainActivityViewModel context, UserModel userModel){
        try {
            File file = new File(context.getApplication().getApplicationContext().getCacheDir(), "user_data");
            FileOutputStream fileOutputStream = new FileOutputStream(file.getPath());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(userModel);
            objectOutputStream.close();


            context.getIsLoaded().postValue(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean dataExists(Context context){
        File file = new File(context.getCacheDir(), "user_data");
        return file.exists();
    }

    public UserModel getUser(Context context){
        UserModel userModel = null;
        File file = new File(context.getCacheDir(), "user_data");

        if(file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file.getPath());
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                userModel = (UserModel) objectInputStream.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            Log.d("CARRR", "not exist");
        }
        return userModel;
    }
}
