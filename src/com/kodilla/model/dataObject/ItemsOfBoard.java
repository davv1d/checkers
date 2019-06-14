package com.kodilla.model.dataObject;

import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;

public class ItemsOfBoard {
    private Pawn pawn;
    private Field[][] board;
    private int x;
    private int y;

    public ItemsOfBoard(Pawn pawn, Field[][] board, int lastPositionX, int lastPositionY) {
        this.pawn = pawn;
        this.board = board;
        this.x = lastPositionX;
        this.y = lastPositionY;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public Field[][] getBoard() {
        return board;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //    public boolean isTheSamePawnType(int x, int y){
//     return board[x][y].hasPawn() && board[x][y].getPawn().isBlack() == pawn.isBlack();
//        //   return board[x][y].hasPawn() && board[x][y].getPawn().getPawnType().equals(pawn.getPawnType());
//    }

}
