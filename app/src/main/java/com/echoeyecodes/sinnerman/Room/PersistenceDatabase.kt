package com.echoeyecodes.sinnerman.Room;

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.echoeyecodes.sinnerman.Models.LikeModel
import com.echoeyecodes.sinnerman.Models.UploadNotificationModel
import com.echoeyecodes.sinnerman.Models.UserModel
import com.echoeyecodes.sinnerman.Models.VideoModel
import com.echoeyecodes.sinnerman.Room.Dao.LikeDao
import com.echoeyecodes.sinnerman.Room.Dao.UploadNotificationDao
import com.echoeyecodes.sinnerman.Room.Dao.UserDao
import com.echoeyecodes.sinnerman.Room.Dao.VideoDao

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
