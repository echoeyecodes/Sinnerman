package com.example.myapplication.Utils;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) == 0){
            outRect.left = IntegerToDp.intToDp(15);
        }
        outRect.right = IntegerToDp.intToDp(15);
    }
}
