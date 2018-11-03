package com.example.tictactoe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {
    game_board activity;
    final IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");


    public SMSReceiver(Context context) {
      //  activity = (game_board) context;
        context.registerReceiver(this, intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage currentMessage = null;
        if (bundle != null)
        {
            final Object[] pdusobj = (Object[]) bundle.get("pdus");

            for (int i = 0; i < pdusobj.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    String format = bundle.getString("format");
                    currentMessage = SmsMessage.createFromPdu((byte[]) pdusobj[i], format);
                }
                else {
                    currentMessage = SmsMessage.createFromPdu((byte[]) pdusobj[i]);
                }
            }
            String senderNum = currentMessage.getDisplayOriginatingAddress();
            String message = currentMessage.getDisplayMessageBody();
           // activity.displayMessage(senderNum,message);
        }
    }
}
