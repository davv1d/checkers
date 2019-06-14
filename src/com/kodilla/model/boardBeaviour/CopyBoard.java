package com.kodilla.model.boardBeaviour;

import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.view.CheckersApp;

public class CopyBoard {

    public static Field[][] invoke(Field[][] board) {
        Field[][] copyOfTheBoard = new Field[CheckersApp.WIDTH][CheckersApp.HEIGHT];
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                try {
                    copyOfTheBoard[x][y] = (Field) board[x][y].clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        return copyOfTheBoard;
    }
}
