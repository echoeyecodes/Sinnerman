package com.example.myapplication.Models;

import androidx.room.Embedded;
import androidx.room.Relation;

public class CommentResponseBody {

    @Embedded
    private UserModel user;

    @Embedded
    private CommentModel comment;

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public CommentModel getComment() {
        return comment;
    }

    public void setComment(CommentModel comment) {
        this.comment = comment;
    }
}
