package com.example.myapplication.Room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import com.example.myapplication.Room.Dao.CommentDao;
import com.example.myapplication.Room.Entities.Comment;

@Database(entities = {Comment.class}, version = 1)
public abstract class PersistenceDatabase {
    private static PersistenceDatabase database;
    public abstract CommentDao commentDao();

    public static synchronized PersistenceDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), PersistenceDatabase.class, "api_persistence")
                    .fallbackToDestructiveMigration().build();
        }
        return database;
    }

}
