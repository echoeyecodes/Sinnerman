package com.example.myapplication.Room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.UserModel;
import com.example.myapplication.Models.VideoModel;
import com.example.myapplication.Room.Dao.CommentDao;
import com.example.myapplication.Room.Dao.UserDao;
import com.example.myapplication.Room.Dao.VideoDao;

@Database(entities = {CommentModel.class, UserModel.class, VideoModel.class}, version = 3)
public abstract class PersistenceDatabase extends RoomDatabase {
    private static PersistenceDatabase database;
    public abstract CommentDao commentDao();
    public abstract UserDao userDao();
    public abstract VideoDao videoDao();

    public static synchronized PersistenceDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), PersistenceDatabase.class, "api_persistence")
                    .fallbackToDestructiveMigration().build();
        }
        return database;
    }

}
