package com.echoeyecodes.sinnerman.Room.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.echoeyecodes.sinnerman.Models.UserModel;

@Dao
public abstract class UserDao {

    @Insert
    public abstract void insertUser(UserModel userModel);

    @Insert
    public abstract void insertUsers(UserModel... userModels);

    @Query("DELETE FROM users")
    public abstract void deleteAllUsers();

}
