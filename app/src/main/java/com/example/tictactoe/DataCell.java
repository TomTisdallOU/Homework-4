package com.example.tictactoe;


import java.util.ArrayList;

import java.util.Observable;

import java.util.Observer;


public class DataCell extends Observable{

    private int symbol;
    public boolean indicator;
    private ArrayList<Observer> observers = new ArrayList<>();


    public void ObservableValue(int symbol){
        this.symbol = symbol;
    }
    public int getSymbol(){
        return symbol;
    }
    public void setSymbol(int symbol){
        this.symbol = symbol;
        this.indicator = true;
        setChanged();
        notifyListeners();
    }

    public void registerObserver(Observer obs){
        this.observers.add(obs);
    }

    public void notifyListeners(){

        for (Observer obs : observers){
            obs.update(new DataCell(),symbol);
        }

    }


}
