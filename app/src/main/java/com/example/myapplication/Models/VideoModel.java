package com.example.myapplication.Models;

public class VideoModel {

    private String id;
    private String title;
    private String thumbnail;
    private int like_count = 0;
    private String video_url;

    public VideoModel(String id, String title, String thumbnail, String video_url) {
        this.id = id;
        this.title = title;
        this.video_url = video_url;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = this.like_count + like_count;
    }

}
