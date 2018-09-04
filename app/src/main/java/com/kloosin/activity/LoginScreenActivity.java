package com.kloosin.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.kloosin.R;
import com.kloosin.dialog.Loading;
import com.kloosin.service.KLRestService;
import com.kloosin.service.model.UserDetails;
import com.kloosin.utility.u.AlertInfoHelper;
import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.RequestHelper;
import com.kloosin.utility.u.SPHelper;
import com.kloosin.utility.u.TagNameHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private Loading mLoader;
    Button button_sigin;
    RelativeLayout relative;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // MAKE THE SPLASH SCREEN A FULL SCREEN VIEW
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // SET THE VIEW
        setContentView(R.layout.activity_login_screen);
        button_sigin = (Button) findViewById(R.id.btn_signin);
        relative  = (RelativeLayout) findViewById(R.id.relative_layout);
        button_sigin.setOnClickListener(this);
        relative.setOnClickListener(this);

        // SET BUTTON ACTION
       // findViewById(R.id.btn_signin).setOnClickListener(this);
        findViewById(R.id.btn_sign_up).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                performLogin();
                break;
            case R.id.btn_sign_up:
                goToSignUpPage();
                break;

            case R.id.relative_layout:
                hideKeyboard(view);
                break;
        }


    }


    /**
     * Make a request for Login
     */
    private void performLogin() {
        if (!KLRestService.getInstance().isNetworkAvailable(getMContext())) {
            AlertInfoHelper.getInstance().alertNetworkError(getMContext());
            return;
        }
        ///getLoader().show();
        //code changes
        progressDialog = new ProgressDialog(LoginScreenActivity.this);
        progressDialog.setMessage("Loading Please wait ....");
        progressDialog.show();

        UserDetails.LoginRequest _request = new UserDetails.LoginRequest();
        _request.setGrantType("password");
        _request.setUsername(String.valueOf(((EditText) findViewById(R.id.username)).getText()));
        _request.setPassword(String.valueOf(((EditText) findViewById(R.id.password)).getText()));
        Call<UserDetails> restService = KLRestService.getInstance().getService(getMContext()).getUserToken(_request.getGrantType(), _request.getUsername(), _request.getPassword());
        restService.enqueue(new Callback<UserDetails>() {
            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if ( response.code() == 200 ) {
                    CommonHelper.getInstance().setCurrentUser(getMContext(), response.body());
                    SPHelper.getInstance(getMContext()).writeBoolean(TagNameHelper.getInstance().NEED_TO_REBOOT_RETROFIT_SERVICE_KEY, true);
                    getLoader().dismiss();

                    goToMainScreen();
                } else {
                    getLoader().dismiss();
                    RequestHelper.getInstance().handleErrorResponse( getMContext(), response.code(), response.errorBody() );
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                getLoader().dismiss();
                RequestHelper.getInstance().handleFailedRequest(call, t);
            }
        });
    }


    /**
     * Authentication completed!
     * Everything looks good to open the APP
     */
    private void goToMainScreen() {
        Intent _intent = new Intent(getMContext(), MainActivity.class);
        startActivity(_intent);
        finish();
    }


    /**
     * User Registration Page
     */
    private void goToSignUpPage() {
        Intent _intent = new Intent(getMContext(), SignUpScreenActivity.class);
        startActivity(_intent);
        finish();
    }

    /**
     * @return Current Screen Context
     */
    public Context getMContext() {
        return LoginScreenActivity.this;
    }


    private Loading getLoader() {

        if (null == mLoader)
            mLoader = new Loading(getMContext());
        return mLoader;
    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

