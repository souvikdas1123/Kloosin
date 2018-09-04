package com.kloosin.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.kloosin.R;
import com.kloosin.dialog.Loading;
import com.kloosin.service.KLRestService;
import com.kloosin.service.model.UserRegistration;
import com.kloosin.utility.u.AlertInfoHelper;
import com.kloosin.utility.u.PermissionRequestHelper;
import com.kloosin.utility.u.RequestHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Loading mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // MAKE THE SPLASH SCREEN A FULL SCREEN VIEW
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_signup_screen);

        // SET BUTTON ACTION
        findViewById( R.id.btn_signin ).setOnClickListener( this );
        findViewById( R.id.btn_sign_up ).setOnClickListener( this );
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View view) {
        switch ( view.getId() ) {
            case R.id.btn_signin:
                goToLoginScreen();
                break;
            case R.id.btn_sign_up:
                if ( PermissionRequestHelper.getInstance().checkPermission(getMContext(), PermissionRequestHelper.Permission.READ_SMS_PERMISSION) ) {
                        completeRegistrationProcess();
                } else {
                    PermissionRequestHelper.getInstance().requestPermission(getMContext(), PermissionRequestHelper.Permission.READ_SMS_PERMISSION, PermissionRequestHelper.Permission.READ_SMS_PERMISSION_CODE, null);
                }
                break;
        }
    }

    private void completeRegistrationProcess() {
        if (!KLRestService.getInstance().isNetworkAvailable(getMContext())) {
            AlertInfoHelper.getInstance().alertNetworkError(getMContext());
            return;
        }
        final UserRegistration.Request _request = new UserRegistration.Request();
        _request.setMobileNo(String.valueOf(((EditText) findViewById(R.id.mobile_no)).getText()));
        _request.setFirstName(String.valueOf(((EditText) findViewById(R.id.first_name)).getText()));
        _request.setLastName(String.valueOf(((EditText) findViewById(R.id.last_name)).getText()));
        _request.setPassword(String.valueOf(((EditText) findViewById(R.id.password)).getText()));
        _request.setConfirmPassword(String.valueOf(((EditText) findViewById(R.id.confirm_password)).getText()));

        getLoader().show();
        Call<Void> _call = KLRestService.getInstance().getService(getMContext()).requestGenerateOTP(_request);
        _call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                getLoader().dismiss();
                if ( response.code() == 200 ) {
                    gotoThankYouPage( _request );
                } else {
                    RequestHelper.getInstance().handleErrorResponse( getMContext(), response.code(), response.errorBody() );
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                getLoader().dismiss();
                RequestHelper.getInstance().handleFailedRequest(call, t);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            switch (requestCode) {
                case PermissionRequestHelper.Permission.READ_SMS_PERMISSION_CODE:
                    boolean _is_all_permission_granted  =   true;
                    for (int index = 0; index < grantResults.length; index++) {
                        if (grantResults[ index ] != PackageManager.PERMISSION_GRANTED) {
                            _is_all_permission_granted  =   false;
                            break;
                        }
                    }
                    if ( _is_all_permission_granted ) {
                        completeRegistrationProcess();   // Just do a double check ;)
                    } else {
                        AlertInfoHelper.getInstance().alertIt(getMContext(), getString(R.string.error_dialog_heading), getString(R.string.no_permission_msg));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void gotoThankYouPage(UserRegistration.Request _request) {
        Intent _intent  =   new Intent( getMContext(), ThankYouActivity.class );
        _intent.putExtra("__REGISTRATION__DATA__", new Gson().toJson(_request));
        startActivity( _intent );
        finish();
    }

    private void goToLoginScreen() {
        Intent _intent  =   new Intent( getMContext(), LoginScreenActivity.class );
        startActivity( _intent );
        finish();
    }

    /**
     * @return Current Screen Context
     */
    public Context getMContext() {
        return SignUpScreenActivity.this;
    }



    private Loading getLoader() {
        if (null == mLoader)
            mLoader = new Loading(getMContext());
        return mLoader;
    }
}
