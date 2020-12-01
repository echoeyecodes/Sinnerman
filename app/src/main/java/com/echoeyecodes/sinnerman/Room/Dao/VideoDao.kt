package com.echoeyecodes.sinnerman.Room.Dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.echoeyecodes.sinnerman.Models.UserModel
import com.echoeyecodes.sinnerman.Models.VideoModel
import com.echoeyecodes.sinnerman.Models.VideoResponseBody

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
    @Query("SELECT * FROM videos INNER JOIN users ON user_id=videos.video_user_id WHERE id = :id")
    abstract fun getVideoLiveData(id:String): LiveData<VideoResponseBody>

    @Transaction
    @Query("SELECT * FROM videos INNER JOIN users ON user_id=videos.video_user_id WHERE id = :id")
    abstract fun getVideo(id:String): VideoResponseBody

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
        val data = ArrayList(videoResponseBody)
        val iterator = data.iterator()
        while (iterator.hasNext()){
            val response = iterator.next()
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
    open fun deleteVideoAndUsers(){
        deleteVideos()
        deleteUsers()
    }

}