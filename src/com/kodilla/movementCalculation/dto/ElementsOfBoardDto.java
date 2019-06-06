package com.kodilla.movementCalculation.dto;

import com.kodilla.oldElements.Field;
import com.kodilla.oldElements.Pawn;

public class ElementsOfBoardDto {
    public Pawn pawn;
    public Field[][] board;
    public int x;
    public int y;

    public ElementsOfBoardDto(Pawn pawn, Field[][] board, int lastPositionX, int lastPositionY) {
        this.pawn = pawn;
        this.board = board;
        this.x = lastPositionX;
        this.y = lastPositionY;
    }

    public boolean isTheSamePawnType(int x, int y){
        return board[x][y].hasPawn() && board[x][y].getPawn().getPawnType().equals(pawn.getPawnType());
    }

}
