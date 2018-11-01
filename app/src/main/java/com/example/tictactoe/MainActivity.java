package com.example.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button playerButton = null;

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
            int player= 0;
            if(playerNumber.equals("Player 1")){
               player = 0;
            }else{
                player = 1;
            }
            players[player] = new Player(playerName, symbol);


            //TODO setup second activity
            if(player == 0){
                Intent intent = new Intent(this, player_form.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("Title", "Player 2");
                startActivityForResult(intent, 1);
            }
            else{
                //Start Game board activity -- pass in player info
                Intent intent = new Intent(this, game_board.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("Player 1 Name", players[0].getName());
                intent.putExtra("Player 1 Symbol",players[0].getSymbol());
                intent.putExtra("Player 2 Name", players[1].getName());
                intent.putExtra("Player 2 Symbol",players[1].getSymbol());

                startActivityForResult(intent, 1);
            }

        }
        else if (resultCode == Activity.RESULT_CANCELED){

        }
    }

}
