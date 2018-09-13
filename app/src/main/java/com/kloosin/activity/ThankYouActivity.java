package com.kloosin.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.gson.Gson;
import com.kloosin.R;
import com.kloosin.dialog.Loading;
import com.kloosin.service.KLRestService;
import com.kloosin.service.model.UserRegistration;
import com.kloosin.utility.u.AlertInfoHelper;
import com.kloosin.utility.u.RequestHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThankYouActivity extends AppCompatActivity {

    private BroadcastReceiver br;
    public final static String BROADCAST_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    private UserRegistration.Request _request;
    private Loading mLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // MAKE THE SPLASH SCREEN A FULL SCREEN VIEW


        setContentView(R.layout.activity_thank_you);

        if ( getIntent().getExtras() != null ) {
            registerSMSBCReceiver();
            _request = new Gson().fromJson( getIntent().getExtras().getString("__REGISTRATION__DATA__"), UserRegistration.Request.class );
        }
    }

    private void registerSMSBCReceiver() {
        br = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                Bundle data = intent.getExtras();
                Object[] pdus = (Object[]) data.get("pdus");
                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    boolean isOurSMS = sender.endsWith("DECORX");
                    String messageBody = smsMessage.getMessageBody();
                    String verificationCode = messageBody.replaceAll("[^0-9]", "");
                    if (isOurSMS == true) {
                        ((EditText) findViewById(R.id.verification_code)).setText( verificationCode );
                        _request.setOtp(verificationCode);
                        completeRegistrationProcess();
                        break;
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter(BROADCAST_ACTION);
        registerReceiver(br, intentFilter);
    }

    private void completeRegistrationProcess() {
        if (!KLRestService.getInstance().isNetworkAvailable(getMContext())) {
            AlertInfoHelper.getInstance().alertNetworkError(getMContext());
            return;
        }
        getLoader().show();
        Call<Void> _call = KLRestService.getInstance().getService(getMContext()).requestUserRegistration(_request);
        _call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                getLoader().dismiss();
                if ( response.code() == 200 ) {
                    goToLoginScreen();
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

    private void goToLoginScreen() {
        Intent _intent  =   new Intent( getMContext(), LoginScreenActivity.class );
        startActivity( _intent );
        finish();
    }

    private Context getMContext() {
        return ThankYouActivity.this;
    }

    private Loading getLoader() {
        if (null == mLoader)
            mLoader = new Loading(getMContext());
        return mLoader;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(br);
        super.onDestroy();
    }
}
