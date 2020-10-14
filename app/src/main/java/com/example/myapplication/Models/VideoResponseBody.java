package com.example.myapplication.Models;

public class VideoResponseBody {
    private VideoModel video;
    private UserModel user;

    public VideoModel getVideo() {
        return video;
    }

    public void setVideo(VideoModel video) {
        this.video = video;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
