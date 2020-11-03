package com.echoeyecodes.sinnerman.Models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity(tableName = "comments")
data class CommentModel(@PrimaryKey @NonNull @ColumnInfo(name = "comment_id") var id: String, var comment: String, var createdAt: String, var status: Int = 0,
                        @ColumnInfo(name = "comment_user_id") var user_id: String, var video_id: String
) {


    constructor(@NotNull id : String, comment : String, createdAt : String, status: Int): this(id, comment, createdAt, status, "", "")
    {
        this.id = id;
        this.comment = comment;
        this.createdAt = createdAt;
    }

}
