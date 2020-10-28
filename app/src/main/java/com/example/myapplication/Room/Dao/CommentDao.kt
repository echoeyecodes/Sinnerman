package com.example.myapplication.Room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.CommentResponseBody;
import com.example.myapplication.Models.UserModel;


@Dao
 abstract class CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     abstract fun insertComment(comment: CommentModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertUser(userModel: UserModel);

    @Insert
     abstract fun insertComments(comment : Array<UserModel>);


    @Query("SELECT * FROM comments INNER JOIN users ON comments.comment_user_id = users.user_id ORDER BY createdAt DESC")
     abstract fun getComments() :  LiveData<List<CommentResponseBody>>

    @Query("SELECT * FROM comments WHERE status = 1")
     abstract suspend fun getUnsentComments() : List<CommentModel>

    @Delete
    abstract suspend fun deleteComment(comment : CommentModel);

    @Query("DELETE FROM comments WHERE status = 0")
     abstract fun deleteAllComment();

    @Transaction
    open fun insertCommentAndUser(commentResponseBody : CommentResponseBody){
        insertUser(commentResponseBody.user);
        insertComment(commentResponseBody.comment);
    }

    @Transaction
    open fun insertCommentAndUsers( commentResponseBody : List<CommentResponseBody>){
        val iterator = commentResponseBody.iterator();
        while (iterator.hasNext()){
            val data = iterator.next()
            insertUser(data.user);
            insertComment(data.comment);
        }
    }

}
