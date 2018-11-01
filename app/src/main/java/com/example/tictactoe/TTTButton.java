package com.example.tictactoe;


import android.content.Context;
import android.content.res.TypedArray;

import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

public class TTTButton extends AppCompatButton implements Observer {

private int buttonImage = 0;
private int buttonPosition = 0;
Button tttButton = null;


    public TTTButton(Context context) {
        super(context);
        init(null,0);

    }

    public TTTButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs,0);

        //inflate the view
     //   LayoutInflater inflater = LayoutInflater.from(context);
     //   ConstraintLayout container = (ConstraintLayout) inflater.inflate(R.layout.ttt_button, this);
      //  tttButton = container.findViewById(R.id.button);


    }

    public TTTButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
     //   this.buttonImage = buttonImage;
      //  this.buttonPosition = buttonPosition;
    }

    public int getButtonImage() {
        return buttonImage;
    }

    public int getButtonPosition() {
        return buttonPosition;
    }

    public void setButtonImage(int buttonImage) {
       // this.buttonImage = buttonImage;
        this.setBackgroundResource(buttonImage);
    }

    public void setButtonPosition(int buttonPosition) {
        this.buttonPosition = buttonPosition;
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TTTButton, defStyle, 0);

        buttonImage = a.getInt(
                R.styleable.TTTButton_image,0);
        buttonPosition = a.getInt(
                R.styleable.TTTButton_position,
                (int)0);



        a.recycle();

    }

    @Override
    public void update(Observable o, Object arg) {
        this.setBackgroundResource((int) arg);
    }
}
