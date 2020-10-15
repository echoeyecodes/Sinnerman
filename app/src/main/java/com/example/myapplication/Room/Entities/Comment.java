package com.example.myapplication.Room.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments")
public class Comment {

    @PrimaryKey @NonNull
    private String id;

    @NonNull
    private String type;

    public Comment(@NonNull String id, @NonNull String type) {
        this.id = id;
        this.type = type;
    }

}
