package com.example.tictactoe;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class game_board extends AppCompatActivity {
    int currentPlayer = 0;
    Player[] players = new Player[2];
    TextView turnLabel = null;
    TTTButton[] tttButton = new TTTButton[9];
    Button startOver = null;
    Button cancel = null;
    Button start = null;

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
    protected void onCreate(Bundle savedInstanceState) {
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

        start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    }
}
