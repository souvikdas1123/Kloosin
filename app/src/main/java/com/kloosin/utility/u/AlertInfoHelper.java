package com.kloosin.utility.u;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kloosin.R;
import com.kloosin.utility.listener.DialogListener;

public class AlertInfoHelper {
    private static AlertInfoHelper mInstance = null;

    public static synchronized AlertInfoHelper getInstance() {
        if (null == mInstance)
            mInstance = newInstance();
        return mInstance;
    }

    private static synchronized AlertInfoHelper newInstance() {
        return new AlertInfoHelper();
    }

    public void toastIt(Context context, final String message) {
        if (!TextUtils.isEmpty(message))
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void snackIt(Context context, final String message) {
        if (!TextUtils.isEmpty(message))
            Snackbar.make(((Activity) context).findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    public void alertIt(Context context, final String title, final String errorMessage) {
        try {
            final Dialog dialogView = new Dialog(context, R.style.PopupAnimation1);
            dialogView.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogView.setContentView(R.layout.custom_dialog_one);
            String _title = !TextUtils.isEmpty(title) ? title : "";
            String _message = !TextUtils.isEmpty(errorMessage) ? errorMessage : "";
            ((TextView) dialogView.findViewById(R.id.infoTitle)).setText(_title);
            ((TextView) dialogView.findViewById(R.id.infoMessage)).setText(_message);
            dialogView.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogView.dismiss();
                }
            });
            dialogView.findViewById(R.id.buttonCancel).setVisibility(View.GONE);
            dialogView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alertIt(Context context, final String title, final String errorMessage, final DialogListener _dialogListener) {
        try {
            final Dialog dialogView = new Dialog(context, R.style.PopupAnimation1);
            dialogView.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogView.setContentView(R.layout.custom_dialog_one);
            dialogView.setCancelable(false);
            String _title = !TextUtils.isEmpty(title) ? title : "";
            String _message = !TextUtils.isEmpty(errorMessage) ? errorMessage : "";
            ((TextView) dialogView.findViewById(R.id.infoTitle)).setText(_title);
            ((TextView) dialogView.findViewById(R.id.infoMessage)).setText(_message);
            dialogView.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _dialogListener.onClickOK();
                    dialogView.dismiss();
                }
            });
            dialogView.findViewById(R.id.buttonCancel).setVisibility(View.GONE);
            dialogView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void confirmIt(Context context, final String title, final String errorMessage, final DialogListener _dialogListener) {
        try {
            final Dialog dialogView = new Dialog(context, R.style.PopupAnimation1);
            dialogView.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogView.setContentView(R.layout.custom_dialog_one);
            String _title = !TextUtils.isEmpty(title) ? title : "";
            String _message = !TextUtils.isEmpty(errorMessage) ? errorMessage : "";
            ((TextView) dialogView.findViewById(R.id.infoTitle)).setText(_title);
            ((TextView) dialogView.findViewById(R.id.infoMessage)).setText(_message);
            dialogView.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _dialogListener.onClickOK();
                    dialogView.dismiss();
                }
            });
            dialogView.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _dialogListener.onClickCancel();
                    dialogView.dismiss();
                }
            });
            dialogView.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void alertNetworkError(Context context) {
        alertIt(context, context.getString(R.string.error_dialog_heading), context.getString(R.string.no_internet_available_msg));
    }
}
