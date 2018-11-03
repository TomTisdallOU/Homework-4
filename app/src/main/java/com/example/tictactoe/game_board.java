package com.example.tictactoe;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class game_board extends AppCompatActivity {
    int currentPlayer = 0;
    Player[] players = new Player[2];
    TextView turnLabel = null;
    TTTButton[] tttButton = new TTTButton[9];
    Button startOver = null;
    Button cancel = null;
    Button inviteToPlay = null;
    EditText phoneNumberText = null;
    SmsManager smsManager = SmsManager.getDefault();


    private static final int[] BUTTON_IDS = {
            R.id.TTTButton1,
            R.id.TTTButton2,
            R.id.TTTButton3,
            R.id.TTTButton4,
            R.id.TTTButton5,
            R.id.TTTButton6,
            R.id.TTTButton7,
            R.id.TTTButton8,
            R.id.TTTButton9,
    };



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                //title= null;
            } else {
                players[0] = new Player(extras.getString("Player 1 Name"), extras.getInt("Player 1 Symbol",0));
                players[1] = new Player(extras.getString("Player 2 Name"), extras.getInt("Player 2 Symbol",0));

            }
        } else {
            //    title = (String) savedInstanceState.getSerializable("Title");
        }

        phoneNumberText = findViewById(R.id.phoneNumberText);

        inviteToPlay = findViewById(R.id.inviteToPlay);
        inviteToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = phoneNumberText.getText().toString();
                String message = "TTTGame,INVITE," + players[0].getName();
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);

                for (int i = 0; i < 9; i++) {
                    tttButton[i].setEnabled(true);
                }
            }
        });

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        startOver = findViewById(R.id.restart);
        startOver.setVisibility(View.INVISIBLE);
        startOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                players[0].reset();
                players[1].reset();
                currentPlayer = 0;
                for (int i = 0; i < 9; i++) {
                    tttButton[i].setButtonImage(0);
                }
                startOver.setVisibility(View.INVISIBLE);
                turnLabel.setText(players[currentPlayer].getName() + " your turn!");
            }
        });

        TTTButton.OnClickListener myMouse =  new TTTButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                TTTButton myButton = findViewById(v.getId());
                int i = myButton.getButtonPosition();
                players[currentPlayer].MarkCell(i);
                tttButton[i].setButtonImage(players[currentPlayer].getSymbol());

                //String phoneNumber = phoneNumberText.getText().toString();
                //String message = "TTTGame,MOVE," + Integer.toString(i);
                //smsManager.sendTextMessage(phoneNumber, null, message, null, null);


                if (players[currentPlayer].winner()) {
                    turnLabel.setText(players[currentPlayer].getName() + " Wins!");
                    startOver.setVisibility(View.VISIBLE);

                }else {

                    //TODO if current = 0 set to 1 else 0  -- try figuring out the remainder to track  # moves
                    if (currentPlayer == 1) {
                        currentPlayer = 0;
                    } else {
                        currentPlayer = 1;
                    }

                    turnLabel.setText(players[currentPlayer].getName() + " your turn!");
                }
            }
        };



        //TODO Implement game board logic
        //TODO create custom button to track symbol and location
        //TODO use GameBoard.java for reference
        turnLabel = findViewById(R.id.turnLabel);
        turnLabel.setText(players[0].getName() + " your turn!");

        for (int i = 0; i < 9; i++) {

            tttButton[i] = (TTTButton) findViewById(BUTTON_IDS[i]);
            tttButton[i].setButtonPosition(i);
            tttButton[i].setOnClickListener(myMouse);
            tttButton[i].setEnabled(false);


            //TODO register player with the button
            players[0].register(tttButton[i],i);
            players[1].register(tttButton[i],i);
        }

        // TODO: This context passed in is the game_board. I'm not sure how to pass in the right context. Because the
        // TODO: dialog message doesn't show if the 2nd player is still at the welcome screen. If both screens are on the GameBoard screen, it works.
        SMSReceiver smsReceiver = new SMSReceiver(this);
        if(!isSmsPermissionGranted())
            requestReadAndSendSmsPermission();

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
