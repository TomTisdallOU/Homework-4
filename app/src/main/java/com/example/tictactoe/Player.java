package com.example.tictactoe;

import java.util.Observer;

public class Player {

    private String name;
    private DataCell[] cells = new DataCell[9];
    private int symbol;


    //TODO -- Determine the winner here.
    public boolean winner(){
        if ((cells[0].indicator && cells[1].indicator && cells[2].indicator)
        || (cells[3].indicator && cells[4].indicator && cells[5].indicator)
        ||(cells[6].indicator && cells[7].indicator && cells[8].indicator)
        ||(cells[0].indicator && cells[3].indicator && cells[6].indicator)
        ||(cells[1].indicator && cells[4].indicator && cells[7].indicator)
        ||(cells[2].indicator && cells[5].indicator && cells[8].indicator)
        ||(cells[0].indicator && cells[4].indicator && cells[8].indicator)
        ||(cells[2].indicator && cells[4].indicator && cells[6].indicator)){
            return true;
        }
        return false;
    }


    public Player(){
        this.name = "";
        this.symbol = 0;
    }

    public Player(String name, int symbol){
        this.name = name;
        this.symbol = symbol;
        for (int i = 0; i < 9; i++) {
            cells[i] = new DataCell();
        }
    }

    public void reset(){
        for (int i = 0; i < 9; i++) {
            cells[i] = new DataCell();
        }
    }

    public void MarkCell( int cellNumber){
        cells[cellNumber].setSymbol(symbol);

        //cells[cellNumber].setSymbol("1");

    }
    public void register(Observer obs, int cellNumber){
        cells[cellNumber].registerObserver(obs);
    }


    public String getName() {return name;}
    public void setName(String name) {
        this.name = name;
    }
    public DataCell[] getCells(){
        return cells;
  }
    public void setDataCell(DataCell[] cells){
        this.cells = cells;
  }
    public int getSymbol(){
        return symbol;
  }
    public void setSymbol(int symbol){
      this.symbol = symbol;
  }

}
