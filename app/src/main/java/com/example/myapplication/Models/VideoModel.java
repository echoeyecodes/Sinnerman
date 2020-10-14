package com.example.myapplication.Models;

public class VideoModel {

    private String id;
    private String title;
    private String description;
    private String createdAt;
    private String updatedAt;
    private String thumbnail;
    private int like_count = 0;
    private String video_url;

    public VideoModel(String id, String title, String thumbnail, String video_url) {
        this.id = id;
        this.title = title;
        this.video_url = video_url;
        this.thumbnail = thumbnail;
    }

    public VideoModel(String id, String title, String description, String createdAt, String updatedAt, String thumbnail, int like_count, String video_url) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.thumbnail = thumbnail;
        this.like_count = like_count;
        this.video_url = video_url;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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
        this.like_count = like_count;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }
}
