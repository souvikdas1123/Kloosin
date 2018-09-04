package com.kloosin.utility.u;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.kloosin.R;
import com.kloosin.utility.listener.DialogListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Class helper for custom permission support from marshmallow
@RequiresApi(api = Build.VERSION_CODES.M)
public class PermissionRequestHelper {

    private static PermissionRequestHelper mInstance = null;

    public static synchronized PermissionRequestHelper getInstance() {
        if ( null == mInstance) mInstance = new PermissionRequestHelper();
        return mInstance;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(Context context, String _permissionRequest) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;
        if (!_permissionRequest.isEmpty()) {
            if (ContextCompat.checkSelfPermission(context, _permissionRequest) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkPermission(Context context, List<String> _permissionRequest) {
        if (!_permissionRequest.isEmpty()) {
            for (int index = 0; index < _permissionRequest.size(); index++) {
                if (ContextCompat.checkSelfPermission(context, _permissionRequest.get(index)) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission(final Context context, final String _permissionRequest, final Integer permissionRequestCode, Fragment _fragment) {
        List _tmp = new ArrayList();
        _tmp.add(_permissionRequest);
        requestPermission(context, _tmp, permissionRequestCode,_fragment);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void requestPermission(final Context context, final List<String> _permissionRequest, final Integer permissionRequestCode, final Fragment _fragment) {
        boolean _displayCustomPrompt = false;
        if (!_permissionRequest.isEmpty()) {
            final List<String> _requestedPermission = new ArrayList<>();
            for (int index = 0; index < _permissionRequest.size(); index++) {
                if (ContextCompat.checkSelfPermission(context, _permissionRequest.get(index)) != PackageManager.PERMISSION_GRANTED) {
                    _requestedPermission.add(_permissionRequest.get(index));
                    if ( SPHelper.getInstance(context).readBoolean(String.valueOf(permissionRequestCode)) &&
                            !ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, _permissionRequest.get(index))) {
                        _displayCustomPrompt = true;
                    }
                }
            }

            SPHelper.getInstance(context).writeBoolean(String.valueOf(permissionRequestCode), true);

            if (!_requestedPermission.isEmpty()) {
                if (_displayCustomPrompt) {
                    AlertInfoHelper.getInstance().alertIt(context, context.getString(R.string.update_permission_dialog_heading), context.getString(R.string.no_permission_msg), new DialogListener() {
                        @Override
                            public void onClickOK() {
                            try {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                                intent.setData(uri);
                                context.startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onClickCancel() {
                            // keep me silent :)
                        }
                    });
                } else {
                    String _message     =    context.getString(R.string._generic_permission_request_message);
                    switch ( permissionRequestCode ) {
                        case Permission.READ_CONTACT_PERMISSION_CODE:
                            _message    =   context.getString(R.string.read_contact_permission_request);
                            break;
                    }
                    AlertInfoHelper.getInstance().alertIt(context, context.getString(R.string.update_permission_dialog_heading), _message, new DialogListener() {
                        @Override
                        public void onClickOK() {
                            if ( null != _fragment ) {
                                _fragment.requestPermissions(_requestedPermission.toArray(new String[_requestedPermission.size()]), permissionRequestCode);
                            } else {
                                ActivityCompat.requestPermissions(((Activity) context), _requestedPermission.toArray(new String[_requestedPermission.size()]), permissionRequestCode);
                            }
                        }

                        @Override
                        public void onClickCancel() {
                            // keep me silent again :)
                        }
                    });
                }
            }
        }
    }


    public static class Permission {

        // Permission for reading contact from phone book
        public static final String READ_CONTACT_PERMISSION    =   Manifest.permission.READ_CONTACTS;
        public static final int READ_CONTACT_PERMISSION_CODE  =   101;

        // Permission for reading storage
        public static final String READ_STORAGE_PERMISSION    =   Manifest.permission.READ_EXTERNAL_STORAGE;
        public static final int READ_STORAGE_PERMISSION_CODE  =   102;

        // Permission for reading sms from phone
        private static String[] _lp= {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};
        public static final List<String> READ_SMS_PERMISSION   =   new ArrayList<String>(Arrays.asList(_lp));
        public static final int READ_SMS_PERMISSION_CODE  =   104;


    }

}
