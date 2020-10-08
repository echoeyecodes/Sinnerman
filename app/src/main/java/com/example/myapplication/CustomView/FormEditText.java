package com.example.myapplication.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

public class FormEditText extends RelativeLayout {

    private TextInputEditText textInputEditText;
    private TextView textView;

    public FormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet){
        inflate(context, R.layout.form_edit_text, this);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.FormEditText, 0, 0);
        initViews();

        try{
            String name = typedArray.getString(R.styleable.FormEditText_text);
            String header = typedArray.getString(R.styleable.FormEditText_header);
            String placeholder = typedArray.getString(R.styleable.FormEditText_placeholder);
            boolean type = typedArray.getBoolean(R.styleable.FormEditText_isSecure, false);

            setTitle(name);
            setHeader(header);
            setPlaceholder(placeholder);
            setInputType(type);

        }finally {
            {
                typedArray.recycle();
            }
        }
    }

    private void refreshLayout(){
        invalidate();
        requestLayout();
    }

    private void initViews(){
        textView = findViewById(R.id.form_text_header);
        textInputEditText = findViewById(R.id.form_edit_text);
    }

    public void setTitle(CharSequence charSequence){
        textInputEditText.setText(charSequence);
        refreshLayout();
    }

    public void setHeader(CharSequence charSequence){
        textView.setText(charSequence);
        refreshLayout();
    }

    public void setPlaceholder(CharSequence charSequence){
        textInputEditText.setHint(charSequence);
        refreshLayout();
    }

    public void setInputType(boolean type){
        if(type){
            textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        refreshLayout();
    }
}
