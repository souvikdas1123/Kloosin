package com.kloosin.kview;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

public class KLTextView extends android.support.v7.widget.AppCompatTextView {
    public KLTextView(Context context) {
        super(context);
        init();
    }

    public KLTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KLTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (isInEditMode()) return;

        try {
            Typeface tf;
            if ( null != getTypeface() && getTypeface().getStyle() == Typeface.BOLD ) {
                tf = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_bold.ttf");
            } else {
                tf = Typeface.createFromAsset(getContext().getAssets(), "font/roboto_regular.ttf");
            }
            setTypeface(tf);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
