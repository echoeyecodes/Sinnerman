package com.example.myapplication.CustomView

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.myapplication.R

class ProfileItem(context: Context, attributeSet: AttributeSet) : LinearLayout(context) {
    lateinit var imageView:ImageView
    lateinit var profileContext:TextView
    lateinit var name:TextView

    init {
        start(context, attributeSet)
    }

    private fun start(context: Context, attributeSet: AttributeSet){
        inflate(context, R.layout.layout_profile_item, this);
        val typedArray = context.theme.obtainStyledAttributes(attributeSet, R.styleable.ProfileItem, 0, 0)

        initViews()

        try {
            val name = typedArray.getString(R.styleable.ProfileItem_profile_text)
            val profileCtx = typedArray.getString(R.styleable.ProfileItem_profile_context)
            val icon = typedArray.getInt(R.styleable.ProfileItem_profile_icon, 0)
            setName(name!!)
            setContext(profileCtx!!)
            setIcon(icon)
        } finally {
            typedArray.recycle()
        }
    }

    private fun initViews(){
        imageView = findViewById(R.id.profile_icon)
        name = findViewById(R.id.profile_name);
        profileContext = findViewById(R.id.profile_context);
    }

    fun setName(value: String){
        name.text = value
        refreshLayout();
    }

    fun setContext(value: String){
        profileContext.text = value
        refreshLayout();
    }

    private fun refreshLayout() {
        invalidate()
        requestLayout()
    }


    fun setIcon(icon:Int){
        when (icon) {
            0 -> imageView.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_person_24))
            1 -> {
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_verified_user_24))
            }
            2 -> {
                imageView.setImageDrawable(context.getDrawable(R.drawable.ic_baseline_alternate_email_24))
            }
        }
        refreshLayout();
    }
}