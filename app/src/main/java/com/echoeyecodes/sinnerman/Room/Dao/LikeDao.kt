package com.echoeyecodes.sinnerman.Room.Dao;

import androidx.room.*;
import com.echoeyecodes.sinnerman.Models.LikeModel


@Dao
abstract class LikeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLike(like: LikeModel)

    @Delete
    abstract fun deleteLike(like: LikeModel)

    @Query("SELECT * FROM likes")
    abstract fun getAllLikes() : List<LikeModel>

}
