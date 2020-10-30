package com.echoeyecodes.sinnerman.Models

import androidx.room.Embedded

data class CommentResponseBody( @Embedded var user: UserModel,
                                @Embedded var comment: CommentModel)