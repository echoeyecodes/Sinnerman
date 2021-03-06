package com.echoeyecodes.sinnerman.Models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class UserModel(@PrimaryKey
                    @NonNull
                     @ColumnInfo(name = "user_id")
                     var id: String,
                     var email: String,
                     var password: String,
                     var username: String,
                     var fullname: String,
                     var profile_url: String) : Serializable{

    companion object{
        val serialVersionUID : Long = 6529685098267757690L;
    }

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