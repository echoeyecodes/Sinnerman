package com.example.myapplication;

import android.content.Context;
import android.os.Parcelable;
import androidx.recyclerview.widget.LinearLayoutManager;

public class CustomLayoutManager extends LinearLayoutManager {

    public CustomLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();

    }
}
