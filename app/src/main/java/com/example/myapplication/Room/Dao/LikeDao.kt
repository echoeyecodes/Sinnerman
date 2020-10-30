package com.example.myapplication.Room.Dao;

import androidx.room.*;
import com.example.myapplication.Models.LikeModel


@Dao
abstract class LikeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLike(like: LikeModel)

    @Delete
    abstract fun deleteLike(like: LikeModel)

    @Query("SELECT * FROM likes")
    abstract fun getAllLikes() : List<LikeModel>

}
