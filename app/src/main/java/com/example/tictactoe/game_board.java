package com.example.tictactoe;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class game_board extends AppCompatActivity
                                implements SMSReceiver.SMSReceiverListener, Fragment.NoticeDialogListener{
    int currentPlayer = 0;
    Player[] players = new Player[2];
    TextView turnLabel = null;
    TTTButton[] tttButton = new TTTButton[9];
    Button startOver = null;
    Button cancel = null;
    Button inviteToPlay = null;
    EditText phoneNumberText = null;
    SmsManager smsManager = SmsManager.getDefault();
    Timer timer = null;
    int seconds = 0;
    int minutes = 0;

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
                turnLabel.setText("No player information");
            } else {
                players[0] = new Player(extras.getString("Player 1 Name"), extras.getInt("Player 1 Symbol",0));
                players[1] = new Player(extras.getString("Player 2 Name"), extras.getInt("Player 2 Symbol",0));
            }
        } else {
            turnLabel.setText("No player information");

        }


        inviteToPlay = findViewById(R.id.inviteToPlay);
        inviteToPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnLabel.setText("Inviting player 2.");
                inviteDialog();
       //TT remove the start game from here - wait for accept to be returned
                //         startGame();

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

                //TODO update the label
                turnLabel.setText("It is " + players[currentPlayer].getName() + " turn!");
            }
        });

        TTTButton.OnClickListener myMouse =  new TTTButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                seconds = 0;
                minutes = 0;
                TTTButton myButton = findViewById(v.getId());
                int i = myButton.getButtonPosition();
                players[currentPlayer].MarkCell(i);
                tttButton[i].setButtonImage(players[currentPlayer].getSymbol());


                if (players[currentPlayer].winner()) {


                    turnLabel.setText(players[currentPlayer].getName() + " Wins!");
                    startOver.setVisibility(View.VISIBLE);
                    if (currentPlayer == 1) {
                        currentPlayer = 0;
                    } else {
                        currentPlayer = 1;
                    }
                    timer.cancel();
                    String phoneNumber= players[currentPlayer].getPhoneNumber();
                    String message = "TTTGame,MOVE," + Integer.toString(i);
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);


                    return;

                }else {

                    //TODO if current = 0 set to 1 else 0  -- try figuring out the remainder to track  # moves
                    changePlayer();
                 //   if (currentPlayer == 1) {
                //        currentPlayer = 0;
                //    } else {
                //        currentPlayer = 1;
                //    }

                //    enableButtons(false);

                 //   turnLabel.setText(players[currentPlayer].getName() + " your turn!");
                }

          //      String phoneNumber = phoneNumberText.getText().toString();
                String phoneNumber= players[currentPlayer].getPhoneNumber();
                String message = "TTTGame,MOVE," + Integer.toString(i);
                smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            }
        };




        turnLabel = findViewById(R.id.turnLabel);
      //  setPlayerTurnTitle("");
       // turnLabel.setText("It is" +  players[0].getName() + " turn.");
        turnLabel.setText("Waiting to start game.");

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

    //TODO I think we can change this to use the current player -- and then switch the current player
    public void UpdateBoard(String senderNum, int i)
    {
        players[currentPlayer].MarkCell(i);
        tttButton[i].setButtonImage(players[currentPlayer].getSymbol());


        if (players[currentPlayer].winner()) {
            turnLabel.setText(players[currentPlayer].getName() + " Wins!");
            startOver.setVisibility(View.VISIBLE);

        }else {

            //TODO if current = 0 set to 1 else 0  -- try figuring out the remainder to track  # moves
            changePlayer();
      //      if (currentPlayer == 1) {
      //          currentPlayer = 0;
      //          enableButtons(true);
      //      } else {
      //          currentPlayer = 1;
      //          enableButtons(false);
      //     }

            turnLabel.setText(players[currentPlayer].getName() + " your turn!");
        }
    }

    public void enableButtons (Boolean enable){
        for (int i = 0; i < 9; i++) {
            tttButton[i].setEnabled(enable);
        }
    }

    //Provided so players can be switched -- enable buttons if this players turn otherwise disable buttons
    public void changePlayer(){
        if (currentPlayer == 0){
            currentPlayer = 1;
            enableButtons(false);
        }else{
            currentPlayer = 0;
            enableButtons(true);
        }
       // setPlayerTurnTitle(players[currentPlayer].getName());
        turnLabel.setText("It is " + players[currentPlayer].getName() + " turn");
    }



    //TODO clean this up -- player 2 gets set up in different places
    public void setPlayer2Info(String name, int symbol, String phoneNumber){
        players[1].setPhoneNumber(phoneNumber);
        players[1].setName(name);
        players[1].setSymbol(symbol);
    }

    public void inviteDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(game_board.this);
        builder.setTitle("Please enter phone number of player to invite");

// Set up the input
        final EditText input = new EditText(game_board.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                players[1].setPhoneNumber(input.getText().toString());
                String message = "TTTGame,INVITE," + players[0].getName();
                smsManager.sendTextMessage(players[1].getPhoneNumber(), null, message, null, null);

                // phoneNumberText =  input.getText().toString();
             //   setPhoneNumber(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public void gameMessageReceived(String action, String msg, String senderNumber) {
        String message = msg;


        switch (action)
        {
      //  if (action == "Invite"){
            case "Invite":
            DialogFragment fragment = new Fragment();
            ((Fragment) fragment).setOtherPlayerName(msg);
            players[1].setName(msg);
            players[1].setPhoneNumber(senderNumber);
            fragment.show(getSupportFragmentManager(), "inviteFragment");

            break;
            case "Accepted":
                players[1].setName(msg);
                turnLabel.setText("It is your turn");
                enableButtons(true);
                startTimer();
                break;

            case "Move":


                UpdateBoard(senderNumber, Integer.parseInt(msg));
                enableButtons(true);
                startTimer();
                break;
        }
    }

    @Override
    public void onDialogPositiveClick() {

        String acceptedMessage = "TTTGame,ACCEPTED," + players[0].getName();
        smsManager.sendTextMessage(players[1].getPhoneNumber(), null, acceptedMessage, null, null);

        //TODO -- I think all of these calls could be pushed to one method in game board
        //Setting player 2 (players[1]) as the current player, changing the title to reflect and setting the buttons disabled
        //((game_board) activity).setPlayer2Info(otherPlayerName, 1, senderNum);
        //setPlayerTurnTitle(players[0].getName());
      //  turnLabel.setText("It is " + players[0] + " turn.");
        currentPlayer = 1;
        turnLabel.setText("It is " + players[1].getName() + " turn.");
        enableButtons(false);
        startTimer();
        //TODO START THE GAME
    }

    @Override
    public void onDialogNegativeClick() {
        //TODO Say he declined
    }

    public void startTimer(){
        timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView tv = findViewById(R.id.viewTimer);
                        tv.setText(String.valueOf(minutes) + ":" + String.valueOf(seconds));
                        seconds += 1;
                        if(seconds == 60){
                            minutes += 1;
                            seconds = 0;

                        }

                    }
                });
            }
        }, 0, 1000);



    }

}
