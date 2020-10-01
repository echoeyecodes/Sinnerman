package com.example.myapplication.Interface;

import com.example.myapplication.Models.MovieModel;

public interface MainActivityContext {
    void openComments();
    void openVideoFragment();
    void openYourVideosFragment();

    void likeVideo(MovieModel movieModel);
}
