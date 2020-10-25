package com.example.myapplication.Models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserModel(@PrimaryKey
                    @NonNull
                     @ColumnInfo(name = "user_id")
                     var id: String,
                     val email: String,
                     var password: String,
                     var username: String,
                     var fullname: String,
                     var profile_url: String){

    constructor(username: String, password: String) : this("", password, username, "", "", ""){
        this.username = username
        this.password = password
    }

    constructor(fullname: String, username: String, email: String, password: String) : this("", password, username, "", "", ""){
        this.username = username
        this.password = password
    }

    constructor() : this("", "", "", ""){
    }
}