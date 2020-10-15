package com.example.myapplication.Models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments")
public class CommentModel{

    @PrimaryKey
    @NonNull
    private String id;
    private String comment;
    private String createdAt;
    private int status=0;
    private String user_id;
    private String video_id;

    public CommentModel(String id, String comment, String createdAt) {
        this.id = id;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

}
