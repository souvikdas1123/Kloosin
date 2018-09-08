package com.kloosin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kloosin.R;
import com.kloosin.kview.KLTextView;
import com.kloosin.service.model.UserDetails;
import com.kloosin.utility.listener.PopupMenuListener;
import com.kloosin.utility.u.CommonHelper;

public class ProfileMenu extends Dialog implements View.OnClickListener {

    private PopupMenuListener mListener;
    KLTextView klTextView;

    public ProfileMenu(Context context, int themeResId) {
        super(context, themeResId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.profile_menu_layout);


        Window window = getWindow();
        if ( null != window ) {
            WindowManager.LayoutParams wlp = window.getAttributes();
            if ( null != wlp ) {
                wlp.gravity = Gravity.TOP | Gravity.RIGHT;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);
            }
        }
        setCanceledOnTouchOutside(true);

        setUserInformation();
        setMenuClickListener();
    }

    private void setUserInformation() {
        UserDetails _user_details = CommonHelper.getInstance().getCurrentUser(getContext());
        if ( null != _user_details ) {
            if ( null != _user_details.getProfileImage() && !_user_details.getProfileImage().isEmpty() ) {
                CommonHelper.getInstance().setImageFromExternalSource(getContext(), findViewById(R.id.profile_image), _user_details.getProfileImage(), false);
            }
            ((TextView) findViewById(R.id.full_name)).setText( _user_details.getFullName());
        }
    }

    private void setMenuClickListener() {
        findViewById(R.id.profile_image).setOnClickListener(this);
        findViewById(R.id.edit_profile_link).setOnClickListener(this);
        //findViewById(R.id.btn_friends).setOnClickListener(this);
        findViewById(R.id.btn_business).setOnClickListener(this);
        findViewById(R.id.btn_jobs).setOnClickListener(this);
        findViewById(R.id.btn_wallet).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);
        findViewById(R.id.btn_tnc).setOnClickListener(this);
        findViewById(R.id.btn_report_problem).setOnClickListener(this);
        findViewById(R.id.btn_about).setOnClickListener(this);
        findViewById(R.id.btn_logout).setOnClickListener(this);
    }

    public void setListener(PopupMenuListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if ( null != mListener ) mListener.onClick(view.getId());
    }
}
