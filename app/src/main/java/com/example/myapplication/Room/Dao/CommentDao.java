package com.example.myapplication.Room.Dao;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.CommentResponseBody;
import com.example.myapplication.Models.UserModel;

import java.util.List;

@Dao
public abstract class CommentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertComment(CommentModel comment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertUser(UserModel userModel);

    @Insert
    public abstract void insertComments(CommentModel... comment);


    @Query("SELECT * FROM comments INNER JOIN comment_users ON comments.user_id = comment_users.id")
    public abstract LiveData<List<CommentResponseBody>> getComments();

    @Query("SELECT * FROM comments WHERE status = 1")
    public abstract List<CommentModel> getUnsentComments();

    @Delete
    abstract void deleteComment(CommentModel comment);

    @Query("DELETE FROM comments WHERE status = 0")
    public abstract void deleteAllComment();

    @Transaction
    public void insertCommentAndUser(CommentResponseBody commentResponseBody){
        insertUser(commentResponseBody.getUser());
        insertComment(commentResponseBody.getComment());
    }

}
