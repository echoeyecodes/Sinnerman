package com.example.myapplication.Room;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.myapplication.Models.*;
import com.example.myapplication.Room.Dao.LikeDao
import com.example.myapplication.Room.Dao.UploadNotificationDao;
import com.example.myapplication.Room.Dao.UserDao;
import com.example.myapplication.Room.Dao.VideoDao;

@Database(entities = [UserModel::class, VideoModel::class, UploadNotificationModel::class, LikeModel::class], version = 1)
abstract class PersistenceDatabase : RoomDatabase () {
    abstract fun userDao() :UserDao
    abstract fun videoDao() : VideoDao
    abstract fun likeDao() : LikeDao
    abstract fun uploadNotificationDao() : UploadNotificationDao

    companion object {
        fun getInstance(context: Context) = Room.databaseBuilder(context.applicationContext, PersistenceDatabase::class.java, "persistence_db")
                .fallbackToDestructiveMigration().build()
    }

}
