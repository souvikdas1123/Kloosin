package com.kloosin.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.kloosin.R;
import com.kloosin.utility.u.CommonHelper;
import com.kloosin.utility.u.SPHelper;
import com.kloosin.utility.u.TagNameHelper;

public class SplashScreenActivity extends AppCompatActivity {

    // SPLASH SCREEN DEFAULT TIME OUT IS 1 SECOND
    private final int DEFAULT_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // MAKE THE SPLASH SCREEN A FULL SCREEN VIEW
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // SET THE VIEW
        setContentView(R.layout.activity_splash_screen);

        // thread to Close SPLASH screen after DEFAULT_TIME_OUT
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkExistingUserDetails();
            }
        }, DEFAULT_TIME_OUT);
    }

    private void checkExistingUserDetails() {
        if (CommonHelper.getInstance().getCurrentUser(getMContext()) != null) {
            goToNextScreen( MainActivity.class );
            return;
        }
        goToNextScreen( LoginScreenActivity.class );
    }


    /**
     * Navigate to Login Screen
     */
    private void goToNextScreen( Class<?> destination) {
        Intent _intent = new Intent(getMContext(), destination);
        startActivity(_intent);
        finish();
    }



    /**
     * @return Splash Screen Context
     */
    public Context getMContext() {
        return this;
    }
}
