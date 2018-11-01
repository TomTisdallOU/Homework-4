package com.example.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class player_form extends AppCompatActivity {
    TextView playerNumber = null;
    EditText nameText = null;
    String[] spinnerSymbolNames;
    int[] spinnerImages;
    Spinner spinner = null;
    Button nextButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_form);
        playerNumber = findViewById(R.id.playerNumber);
        nameText = findViewById(R.id.nameText);
        spinner = findViewById(R.id.spinner);
        nextButton = findViewById(R.id.nextButton);

        String title = null;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                title= null;
            } else {
                title= extras.getString("Title");
            }
        } else {
            title = (String) savedInstanceState.getSerializable("Title");
        }

        playerNumber.setText(title);
        nameText.setText(title);

        //Set up Spinner
        spinnerSymbolNames = new String[] {"Red Dragon", "Black Dragon", "Blue Dragon",
                "Green Dragon", "Purple Dragon"};
        spinnerImages = new int[] {R.drawable.red_dragon, R.drawable.black_dragon, R.drawable.blue_dragon,
                R.drawable.green_dragon, R.drawable.purple_dragon};
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, spinnerSymbolNames, spinnerImages);

        spinner.setAdapter(spinnerAdapter);



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String playerName =nameText.getText().toString();
                int image = spinnerImages[spinner.getSelectedItemPosition()];
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("PlayerName", playerName);
                intent.putExtra("SymbolNumber", image);
                intent.putExtra("Title", playerNumber.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();

            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent){
        String title = null;

        if(intent.hasExtra("Title")) {
            title = intent.getStringExtra("Title");
        }
        playerNumber.setText(title);
    }
}



