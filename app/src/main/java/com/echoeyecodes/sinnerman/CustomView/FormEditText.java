package com.echoeyecodes.sinnerman.CustomView;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import com.echoeyecodes.sinnerman.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static com.google.android.material.textfield.TextInputLayout.END_ICON_NONE;
import static com.google.android.material.textfield.TextInputLayout.END_ICON_PASSWORD_TOGGLE;

public class FormEditText extends RelativeLayout {

    private TextInputLayout textInputLayout;
    private TextInputEditText textInputEditText;
    private final FormEditTextListener formEditTextListener;
    private String form_type;

    public FormEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        formEditTextListener = (FormEditTextListener) context;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet){
        inflate(context, R.layout.layout_form_edit_text, this);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.FormEditText, 0, 0);
        initViews();

        try{
            String name = typedArray.getString(R.styleable.FormEditText_text);
            form_type = typedArray.getString(R.styleable.FormEditText_form_type);
            String hint = typedArray.getString(R.styleable.FormEditText_hint);
            int type = typedArray.getInt(R.styleable.FormEditText_inputType, 0);
            boolean isSecure = typedArray.getBoolean(R.styleable.FormEditText_isSecure, false);

            setText(name);
            setHint(hint);
            setInputType(type);
            setEndIconMode(isSecure);

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

    private void setEndIconMode(boolean value){
        textInputLayout.setEndIconMode(value ? END_ICON_PASSWORD_TOGGLE : END_ICON_NONE);
    }

    private void initViews(){
        textInputLayout = findViewById(R.id.form_text_input_layout);
        textInputEditText = findViewById(R.id.form_text_input);

        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                formEditTextListener.onTextInput(form_type, s.toString());
            }
        });

        textInputEditText.setOnFocusChangeListener((v, hasFocus) -> formEditTextListener.onFocused(form_type, hasFocus));
    }

    public void setHint(CharSequence charSequence){
        textInputLayout.setHint(charSequence);
        refreshLayout();
    }

    public void setText(CharSequence charSequence){
        textInputEditText.setText(charSequence);
        refreshLayout();
    }

    public void setError(boolean enable, String message){
        textInputLayout.setErrorEnabled(enable);
        textInputLayout.setError(message);
        refreshLayout();
    }

    public void setInputType(int type){
        switch (type){
            case 0:
                textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                break;
            case 1:
                textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
                break;
            case 2:
                textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case 3:
                textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                break;
        }
        refreshLayout();
    }
}
