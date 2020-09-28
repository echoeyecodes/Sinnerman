package com.example.myapplication.Utils;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomItemDecoration extends RecyclerView.ItemDecoration {
    private int top;
    private int left;
    private int bottom;
    private int right;



    public CustomItemDecoration(int space){
        this.top = space;
        this.bottom = space;
        this.left = space;
        this.right = space;
    }

    public CustomItemDecoration(int vertical, int horizontal){
        this.top = vertical;
        this.bottom = vertical;
        this.left = horizontal;
        this.right = horizontal;
    }

    public CustomItemDecoration(int top, int right, int bottom, int left){
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
    }


    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = left;
        outRect.right = right;
        outRect.top = top;
        outRect.bottom = bottom;
    }

}
