package com.example.myapplication.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.Models.CommentModel
import com.example.myapplication.Models.UploadNotificationModel
import com.example.myapplication.Models.UserModel
import com.example.myapplication.Models.VideoModel
import com.example.myapplication.Room.Dao.CommentDao
import com.example.myapplication.Room.Dao.UserDao

@Database(entities = [UserModel::class, CommentModel::class, VideoModel::class, UploadNotificationModel::class], version = 1)
abstract class CommentDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao?
    abstract fun userDao(): UserDao?

    companion object {
        private var database: CommentDatabase? = null
        @Synchronized
        fun getInstance(context: Context): CommentDatabase? {
            if (database == null) {
                database = Room.databaseBuilder(context.applicationContext, CommentDatabase::class.java, "comment_database")
                        .fallbackToDestructiveMigration().build()
            }
            return database
        }
    }
}