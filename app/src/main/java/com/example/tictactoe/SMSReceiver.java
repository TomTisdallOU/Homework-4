package com.example.tictactoe;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver
{
    game_board activity = null;
    final IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    SmsManager smsManager = SmsManager.getDefault();
    String senderNum = null;


    public SMSReceiver(Context context) {
        activity = (game_board) context;
        context.registerReceiver(this, intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage currentMessage = null;

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        String acceptedMessage = "TTTGame,ACCEPTED," + activity.players[1].getName();
                        smsManager.sendTextMessage(senderNum, null, acceptedMessage, null, null);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        String deniedMessage = "TTTGame,DENIED," + activity.players[1].getName();
                        smsManager.sendTextMessage(senderNum, null, deniedMessage, null, null);
                        break;
                }
            }
        };

        if(bundle != null)
        {
            final Object[] pdusObj = (Object[]) bundle.get("pdus");

            for (int i = 0; i < pdusObj.length; i++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    String format = bundle.getString("format");
                    currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                }
                else {
                    //currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                }
            }
            senderNum = currentMessage.getDisplayOriginatingAddress();
            String message = currentMessage.getDisplayMessageBody();
            String[] tokens = message.split(",");
            if(tokens[0].equals("TTTGame"))
            {
                switch(tokens[1])
                {
                    case "INVITE":
                        // TODO: This context passed in is the game_board. I'm not sure how to pass in the right context. Because the
                        // TODO: dialog message doesn't show
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("You are invited by " + activity.players[0].getName() + " to play Tic Tac Toe game. Do you accept this invitation?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                        break;
                    case "ACCEPTED":
                        // Keep playing game. Player 1 makes first move
                        break;
                    case "DENIED":
                        // Go back to welcome screen
                        break;
                    case "MOVE":
                        //
                        break;
                    default: break;
                }
            }
            //activity.displayMessage(senderNum, message);
        }
    }
}
