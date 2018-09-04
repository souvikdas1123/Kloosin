package com.kloosin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Window;

import com.kloosin.R;

public class Loading extends Dialog {

    public Loading(@NonNull Context context) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_layout);
    }

    @Override
    public void show() {
        if (!isShowing())
            super.show();
    }

    @Override
    public void dismiss() {
        if (isShowing())
            super.dismiss();
    }
}
