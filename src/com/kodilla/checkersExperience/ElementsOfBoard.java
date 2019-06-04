package com.kodilla.checkersExperience;

import com.kodilla.view.Field;
import com.kodilla.view.Pawn;

public class ElementsOfBoard {
    public Pawn pawn;
    public Field[][] board;
    public int x;
    public int y;

    public ElementsOfBoard(Pawn pawn, Field[][] board, int lastPositionX, int lastPositionY) {
        this.pawn = pawn;
        this.board = board;
        this.x = lastPositionX;
        this.y = lastPositionY;
    }

    public boolean isTheSamePawnType(int x, int y){
        return board[x][y].hasPawn() && board[x][y].getPawn().getPawnType().equals(pawn.getPawnType());
    }

}
