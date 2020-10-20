package com.example.myapplication.Room.Dao

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.example.myapplication.Models.UserModel
import com.example.myapplication.Models.VideoModel
import com.example.myapplication.Models.VideoResponseBody

@Dao
abstract class VideoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertVideos(videos: VideoModel)

    @Query("SELECT * FROM videos INNER JOIN users ON videos.video_user_id = users.user_id LIMIT :limit OFFSET :offset")
    abstract fun getVideos(offset:Int, limit:Int): List<VideoResponseBody>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertUser(userModel : UserModel)

    @Query("DELETE FROM videos")
    abstract fun deleteVideos()

    @Query("DELETE FROM users")
    abstract fun deleteUsers()

    @Transaction
    open suspend fun insertVideoAndUsers(videoResponseBody : VideoResponseBody){
        insertVideos(videoResponseBody.video)
        insertUser(videoResponseBody.user)
    }

    @Transaction
    open suspend fun deleteVideoAndUsers(){
        deleteVideos()
        deleteUsers()
    }

}