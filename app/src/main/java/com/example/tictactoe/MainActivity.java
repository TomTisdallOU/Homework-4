package com.example.tictactoe;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button playerButton = null;
    Player[] players = new Player[2];
    SmsManager smsManager = SmsManager.getDefault();



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


    }

    @Override
    protected void onNewIntent(Intent intent) {

      //  String firstName = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        if(resultCode== Activity.RESULT_OK){
            String playerNumber = data.getStringExtra("Title");
            String playerName = data.getStringExtra("PlayerName");
            int symbol = data.getIntExtra("SymbolNumber",0);
            String playerPhoneNumber = data.getStringExtra("PhoneNumber");
            //TODO  I wonder if we can get player 1's phone number by a lookup

            int player= 0;

            //TODO I dont think we need this code.
         //   if(playerNumber.equals("Player 1")){
        //       player = 0;
         //   }else{
          //      player = 1;
        //    }


            players[0] = new Player(playerName, symbol, "0");
            players[0] = new Player("Player2", 0, playerPhoneNumber);


            //TODO get phone number, send invite message to opponent
            String phoneNumber = players[1].getPhoneNumber();
            String message = "%$$^ | TTTGame | INVITE | " + players[0].getName();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

            SMSReceiver smsReceiver = new SMSReceiver(this);
            if(!isSMSPermissionGranted())
                requestReadandSendSMSPermission();



            //  if(player == 0){
         //       Intent intent = new Intent(this, player_form.class );
          //      intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
          //      intent.putExtra("Title", "Player 2");
          //      startActivityForResult(intent, 1);
       //     }
        //    else{
                //Start Game board activity -- pass in player info
                Intent intent = new Intent(this, game_board.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("Player 1 Name", players[0].getName());
                intent.putExtra("Player 1 Symbol",players[0].getSymbol());
                intent.putExtra("Player 1 Phone Number", players[0].getPhoneNumber());
                intent.putExtra("Player 2 Name", players[1].getName());
                intent.putExtra("Player 2 Symbol",players[1].getSymbol());
                intent.putExtra("Player 2 Phone Number", players[1].getPhoneNumber());


                startActivityForResult(intent, 1);
       //     }

        }
        else if (resultCode == Activity.RESULT_CANCELED){

        }
    }
    public boolean isSMSPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED ;
    }

    public void requestReadandSendSMSPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)){

        }
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                Manifest.permission.READ_PHONE_STATE}, 1);
    }

}
