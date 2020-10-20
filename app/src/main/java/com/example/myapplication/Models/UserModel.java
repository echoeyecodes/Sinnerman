package com.example.myapplication.Models;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserModel {

    @PrimaryKey @NonNull
    @ColumnInfo(name = "user_id")
    public String id;

    @Ignore
    private String email;
    @Ignore
    private String password;

    private String username;
    private String fullname;
    private String profile_url;

    public UserModel(String fullname, String username, String email, String password, String profile_url) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.profile_url = profile_url;
        this.password = password;
    }

    public UserModel(){

    }

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getFullname() {
        return fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getProfile_url() {
        return profile_url;
    }

    public void setProfile_url(String profile_url) {
        this.profile_url = profile_url;
    }
}
