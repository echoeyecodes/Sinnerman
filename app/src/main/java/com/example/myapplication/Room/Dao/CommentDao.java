package com.example.myapplication.Room.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.myapplication.Room.Entities.Comment;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert
    void insertComment(Comment comment);

    @Query("SELECT * FROM comments")
    LiveData<List<Comment>> getComments();

    @Delete
    void deleteComment(Comment comment);

}
