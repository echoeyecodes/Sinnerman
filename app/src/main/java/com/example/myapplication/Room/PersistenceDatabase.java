package com.example.myapplication.Room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.Room.Dao.CommentDao;
import com.example.myapplication.Room.Dao.UserDao;

@Database(entities = {CommentModel.class, UserModel.class}, version = 1)
public abstract class PersistenceDatabase extends RoomDatabase {
    private static PersistenceDatabase database;
    public abstract CommentDao commentDao();
    public abstract UserDao userDao();

    public static synchronized PersistenceDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), PersistenceDatabase.class, "api_persistence")
                    .fallbackToDestructiveMigration().build();
        }
        return database;
    }

}
