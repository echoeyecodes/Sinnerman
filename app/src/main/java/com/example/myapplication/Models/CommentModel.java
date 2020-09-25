package com.example.myapplication.Models;

public class CommentModel {
    private String id;
    private String comment;
    private String time_stamp;
    private String name;

    public CommentModel(String id, String comment, String time_stamp, String name) {
        this.id = id;
        this.comment = comment;
        this.time_stamp = time_stamp;
        this.name = name;
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

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
