package com.echoeyecodes.sinnerman.Room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.echoeyecodes.sinnerman.Models.CommentModel;
import com.echoeyecodes.sinnerman.Models.CommentResponseBody;
import com.echoeyecodes.sinnerman.Models.UserModel;


@Dao
 abstract class CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertComment(comment: CommentModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(userModel: UserModel)

    @Update
    abstract fun updateComment(comment: CommentModel)

    @Update
    abstract fun updateUser(userModel: UserModel)


    @Query("SELECT * FROM comments INNER JOIN users ON comments.comment_user_id = users.user_id WHERE comments.video_id = :id ORDER BY createdAt DESC")
    abstract fun getComments(id:String) :  LiveData<List<CommentResponseBody>>

    @Query("SELECT * FROM comments WHERE status = 1")
    abstract suspend fun getUnsentComments() : List<CommentModel>

    @Delete
    abstract suspend fun deleteComment(comment : CommentModel)

    @Query("DELETE FROM comments WHERE status = 0")
    abstract fun deleteAllComment();

    @Query("DELETE FROM users")
    abstract fun deleteAllUsers();


    @Transaction
    open fun deleteAllCommentData(){
        deleteAllComment()
        deleteAllUsers()
    }


    @Transaction
    open fun insertCommentAndUser(commentResponseBody : CommentResponseBody){
        insertUser(commentResponseBody.user)
        insertComment(commentResponseBody.comment)
    }

    @Transaction
    open fun updateCommentAndUser(commentResponseBody : CommentResponseBody){
        updateUser(commentResponseBody.user)
        updateComment(commentResponseBody.comment)
    }

    @Transaction
    open fun insertCommentAndUsers( commentResponseBody : List<CommentResponseBody>){
        val iterator = commentResponseBody.iterator()
        while (iterator.hasNext()){
            val data = iterator.next()
            insertUser(data.user)
            insertComment(data.comment)
        }
    }

}
