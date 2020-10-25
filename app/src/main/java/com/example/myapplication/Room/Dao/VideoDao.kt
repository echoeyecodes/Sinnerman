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

    @Update
    abstract suspend fun updateVideo(video: VideoModel)

    @Update
    abstract suspend fun updateUser(user: UserModel)

    @Transaction
    @Query("SELECT * FROM videos INNER JOIN users ON user_id=videos.video_user_id")
    abstract fun getVideos(): LiveData<List<VideoResponseBody>>

    @Transaction
    @Query("SELECT * FROM videos INNER JOIN users ON user_id=videos.video_user_id")
    abstract fun getVideosList(): List<VideoResponseBody>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertUser(userModel : UserModel)

    @Query("DELETE FROM videos")
    abstract fun deleteVideos()

    @Query("DELETE FROM users")
    abstract fun deleteUsers()

    @Transaction
    open suspend fun insertVideoAndUsers(videoResponseBody : List<VideoResponseBody>){
        for(response in videoResponseBody){
            insertVideos(response.video)
            insertUser(response.user)
        }
    }

    @Transaction
    open suspend fun insertVideoAndUser(videoResponseBody : VideoResponseBody){
        insertVideos(videoResponseBody.video)
        insertUser(videoResponseBody.user)
    }

    @Transaction
    open suspend fun updateVideoAndUser(videoResponseBody : VideoResponseBody){
        updateVideo(videoResponseBody.video)
        updateUser(videoResponseBody.user)
    }

    @Transaction
    open suspend fun deleteVideoAndUsers(){
        Log.d("CARRR", "Deleting data")
        deleteVideos()
        deleteUsers()
    }

}