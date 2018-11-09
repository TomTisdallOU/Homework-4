package com.example.tictactoe;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements SMSReceiver.SMSReceiverListener
        , Fragment.NoticeDialogListener
{

    Button playerButton = null;

    SmsManager smsManager = SmsManager.getDefault();
    Player[] players = new Player[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Set up ToolBar
        //TODO -- not sure why it shows the wrong title -- not using the tool bar I configured
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("Testing");
            //  toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerButton = findViewById(R.id.startButton);

        playerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), player_form.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("Title", "Player 1");
                startActivityForResult(intent, 1);


            }
        });

        SMSReceiver smsReceiver = new SMSReceiver(this);
        if(!isSmsPermissionGranted())
            requestReadAndSendSmsPermission();
    }

    @Override
    protected void onNewIntent(Intent intent) {

        //  String firstName = null;
    }
//TODO listen for invite -- show toast?


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode== Activity.RESULT_OK){
          //  String playerNumber = data.getStringExtra("Title");
            String playerName = data.getStringExtra("PlayerName");
            int symbol = data.getIntExtra("SymbolNumber",0);
         //   int player= 0;
         //   if(playerNumber.equals("Player 1")){
         //       player = 0;
         //   }else{
         //       player = 1;
         //   }
            players[0] = new Player(playerName, symbol);
            players[1] = new Player("Second Player", getResources().getIdentifier("black_dragon", "drawable", getPackageName()));


            //TODO setup second activity
      //      if(player == 0){
      //          Intent intent = new Intent(this, player_form.class );
       //         intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
       //         intent.putExtra("Title", "Player 2");
       //         startActivityForResult(intent, 1);
       //     }
      //      else{
                //Start Game board activity -- pass in player info
        startBoard(false);
       //     }

        }
        else if (resultCode == Activity.RESULT_CANCELED){

        }
    }

    @Override
    public void gameMessageReceived(String action, String msg, String senderNumber) {
        if (action == "Invite"){
            DialogFragment fragment = new Fragment();
            ((Fragment) fragment).setOtherPlayerName(msg);


            players[0] = new Player("Player 2", getResources().getIdentifier("red_dragon", "drawable", getPackageName()));
            players[1] = new Player(msg, getResources().getIdentifier("black_dragon", "drawable", getPackageName()));
            players[1].setPhoneNumber(senderNumber);


            fragment.show(getSupportFragmentManager(), "inviteFragment");




        }
    }

    public void startBoard(boolean acceptedInvite){
        Intent intent = new Intent(this, game_board.class );
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent.putExtra("Player 1 Name", players[0].getName());
        intent.putExtra("Player 1 Symbol",players[0].getSymbol());
        intent.putExtra("Player 2 Name", players[1].getName());
        intent.putExtra("Player 2 Symbol",players[1].getSymbol());
        intent.putExtra("AcceptedInvite",acceptedInvite);

        startActivityForResult(intent, 1);
    }

    @Override
    public void onDialogPositiveClick() {

        String acceptedMessage = "TTTGame,ACCEPTED," + players[0].getName();
        smsManager.sendTextMessage(players[1].getPhoneNumber(), null, acceptedMessage, null, null);
        startBoard(true);
    }

    @Override
    public void onDialogNegativeClick() {

    }
    /** * Check if we have SMS permission */
    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    /** * Request runtime SMS permission */
    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_PHONE_STATE}, 1);
    }

}
