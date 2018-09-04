package com.kloosin.kview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;


public class KLEditText extends android.support.v7.widget.AppCompatEditText {
    public KLEditText(Context context) {
        super(context);
        init();
    }

    public KLEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KLEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) return;

        Typeface tf;
        if ( getTypeface().getStyle() == Typeface.BOLD ) {
            tf = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_bold.ttf");
        } else {
            tf = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_regular.ttf");
        }
        setTypeface(tf);

        try {
            if ( null != getBackground() ) getBackground().clearColorFilter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
