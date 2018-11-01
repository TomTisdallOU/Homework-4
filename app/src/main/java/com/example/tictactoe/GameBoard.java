/*package com.example.tictactoe;

import javafx.beans.Observable;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class GameBoard extends Applet {

    //declaration
    JPanel topPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JPanel bottomPanel = new JPanel();
    JLabel turnLabel = new JLabel();
   // Label turnLabel = new Label();   Updated because the class used JLabel instead of Label
    Button startButton = new Button();
    TTTButton[] tttButton = new TTTButton[9];



    int current = 0;
    Player[] players = new Player[2];



    @Override
    public void init() {


        super.init();
        resize(250, 430);

        MouseListener myMouse =  new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                TTTButton myButton = (TTTButton) e.getSource();
                int i = myButton.getButtonIndex();
                players[current].MarkCell(i);
                if (players[current].winner()){
                    turnLabel.setText("Winner Winner Chicken Dinner!!!!!!!");

                }

                //TODO if current = 0 set to 1 else 0  -- try figuring out the remainder to track  # moves
                if (current == 1) {
                    current = 0;
                } else {
                    current = 1;
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
        add(topPanel);
        add(boardPanel);
        add(bottomPanel);

        //Add top panel and components
        topPanel.setPreferredSize(new Dimension(250, 90));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(turnLabel);
        turnLabel.setText("Game is not started yet!!!");

        //add center board
        boardPanel.setPreferredSize(new Dimension(250, 250));
        boardPanel.setLayout((new FlowLayout(FlowLayout.LEFT, 10, 10)));

        for (int i = 0; i < 9; i++) {
            tttButton[i] = new TTTButton(i);
            tttButton[i].setPreferredSize(new Dimension(70, 70));
            tttButton[i].addMouseListener(myMouse);
            tttButton[i].setEnabled(false);

            boardPanel.add(tttButton[i]);
        }

        //Add Bottom Layout
        bottomPanel.setPreferredSize(new Dimension(250, 90));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        startButton.setLabel("Start Game");
        startButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                players[0] = new Player("Player 1", "X") ;
                players[1] = new Player("Player 2", "O");
                turnLabel.setText("Player 1's Turn");
                current = 0;
                for (int i = 0; i < 9; i++) {
                    players[0].register(tttButton[i], i);
                    players[1].register(tttButton[i], i);
                    tttButton[i].setEnabled(true);
                    tttButton[i].setLabel("");
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }



        });


        bottomPanel.add(startButton);

    }
}

*/