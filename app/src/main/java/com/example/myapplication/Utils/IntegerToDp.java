package com.example.myapplication.Utils;

import android.content.res.Resources;
import android.util.TypedValue;

public class IntegerToDp {

    public static int intToDp(int value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, Float.parseFloat(String.valueOf(value)), Resources.getSystem().getDisplayMetrics());
    }

}
