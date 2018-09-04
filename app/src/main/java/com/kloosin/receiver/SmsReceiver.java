package com.kloosin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.kloosin.utility.listener.SmsListener;

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;
    Boolean isOurSMS = false;
    String verificationCode;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            isOurSMS = sender.endsWith("WNRCRP");
            String messageBody = smsMessage.getMessageBody();
            verificationCode = messageBody.replaceAll("[^0-9]", "");
            if (isOurSMS == true) {
                mListener.messageReceived(verificationCode);
            }
        }
    }
}
