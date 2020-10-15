package com.example.myapplication.Utils;

import android.content.Context;
import android.util.Log;
import com.example.myapplication.MainActivity;

import java.io.*;

public class AuthUser implements Serializable {
    private String name;
    private String profile_url;
    private String username;
    private String id;
    private static final long serialVersionUID = 6529685098267757690L;

    public AuthUser(String id, String name, String profile_url, String username) {
        this.name = name;
        this.profile_url = profile_url;
        this.username = username;
        this.id = id;
    }

    public AuthUser(){

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void saveUser(Context context, AuthUser authUser){
        try {
            File file = new File(context.getCacheDir(), "user_data");
            FileOutputStream fileOutputStream = new FileOutputStream(file.getPath());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(authUser);
            objectOutputStream.close();

            MainActivity mainActivity = (MainActivity) context;
            mainActivity.beginActivity();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean dataExists(Context context){
        File file = new File(context.getCacheDir(), "user_data");
        return file.exists();
    }

    public AuthUser getUser(Context context){
        AuthUser authUser = null;
        File file = new File(context.getCacheDir(), "user_data");

        if(file.exists()){
            try {
                FileInputStream fileInputStream = new FileInputStream(file.getPath());
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                authUser = (AuthUser) objectInputStream.readObject();
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
        return authUser;
    }
}
