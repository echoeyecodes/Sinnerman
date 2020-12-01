package com.echoeyecodes.sinnerman.Room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.echoeyecodes.sinnerman.Models.CommentModel
import com.echoeyecodes.sinnerman.Models.UserModel
import com.echoeyecodes.sinnerman.Room.Dao.CommentDao
import com.echoeyecodes.sinnerman.Room.Dao.UserDao

@Database(entities = [UserModel::class, CommentModel::class], version = 1)
abstract class CommentDatabase : RoomDatabase() {
    abstract fun commentDao(): CommentDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: CommentDatabase? = null
        fun getInstance(context: Context) = INSTANCE ?: synchronized(this) {
            val instance =Room.databaseBuilder(context.applicationContext, CommentDatabase::class.java, "comment_database")
                    .fallbackToDestructiveMigration().build()
            INSTANCE = instance
            return instance
        }
    }

}