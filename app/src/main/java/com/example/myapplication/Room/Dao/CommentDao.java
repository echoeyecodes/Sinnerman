package com.example.myapplication.Room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.myapplication.Models.CommentModel;
import com.example.myapplication.Models.CommentResponseBody;
import com.example.myapplication.Models.UserModel;

import java.util.List;

@Dao
public abstract class CommentDao {

    @Insert
    public abstract void insertComment(CommentModel comment);

    @Insert
    abstract void insertUser(UserModel userModel);

    @Insert
    public abstract void insertComments(CommentModel... comment);

    @Query("SELECT * FROM comments")
    public abstract LiveData<List<CommentResponseBody>> getComments();

    @Delete
    abstract void deleteComment(CommentModel comment);

    @Query("DELETE FROM comments")
    public abstract void deleteAllComment();

    @Transaction
    public void insertCommentAndUser(CommentResponseBody commentResponseBody){
        insertUser(commentResponseBody.getUser());
        insertComment(commentResponseBody.getComment());
    };

}
