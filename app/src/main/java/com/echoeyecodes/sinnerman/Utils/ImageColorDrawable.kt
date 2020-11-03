package com.echoeyecodes.sinnerman.Utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable

class ImageColorDrawable {

    companion object{
        fun getInstance() = ColorDrawable(Color.parseColor("#33000000"))
    }
}