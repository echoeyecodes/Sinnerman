package com.echoeyecodes.sinnerman.Paging

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.echoeyecodes.sinnerman.R

class AdsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val linearLayout: LinearLayout = itemView.findViewById(R.id.ad_layout_container);
    val imageView:ImageView = itemView.findViewById(R.id.ads_image)

    init {
        val displayMetrics = Resources.getSystem().displayMetrics
        val imageParams = imageView.layoutParams as LinearLayout.LayoutParams
        imageParams.width = (displayMetrics.widthPixels * 0.8).toInt()
        imageParams.height = (displayMetrics.heightPixels / 3.5).toInt()
        imageView.layoutParams = imageParams
    }
}