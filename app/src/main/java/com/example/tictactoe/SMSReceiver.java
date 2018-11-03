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
import android.view.View;

public class SMSReceiver extends BroadcastReceiver
{
    Context activity = null;
    final IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
    SmsManager smsManager = SmsManager.getDefault();
    String senderNum = null;


    public SMSReceiver(Context context) {
        activity = context;
        context.registerReceiver(this, intentFilter);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage currentMessage = null;

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
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("You are invited by " + ((game_board)activity).players[0].getName() + " to play Tic Tac Toe game. Do you accept this invitation?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                        break;
                    case "ACCEPTED":
                        // Keep playing game. Player 1 makes first move
                        break;
                    case "DENIED":
                        // Go back to welcome screen
                        break;
                    case "MOVE":
                        //TODO: While in 2nd emulator, you can't click on any of the buttons. Can only do it in the 1 emulator. That's a problem
                        ((game_board)activity).UpdateBoard(senderNum, Integer.parseInt(tokens[2]));
                        break;
                    default: break;
                }
            }
            //activity.displayMessage(senderNum, message);
        }
    }


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    String acceptedMessage = "TTTGame,ACCEPTED," + ((game_board)activity).players[1].getName();
                    smsManager.sendTextMessage(senderNum, null, acceptedMessage, null, null);
                    // Go to game_board screen
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    String deniedMessage = "TTTGame,DENIED," + ((game_board)activity).players[1].getName();
                    smsManager.sendTextMessage(senderNum, null, deniedMessage, null, null);
                    break;
            }
        }
    };
}
