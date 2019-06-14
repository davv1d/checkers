package com.kodilla.model.stateOfTheGame;

import com.kodilla.model.boardBeaviour.CopyBoard;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;

public class DefaultSettings {

    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private Field[][] logicBoard = new Field[WIDTH][HEIGHT];
    private boolean[][] fieldsBoard = new boolean[WIDTH][HEIGHT];
    private Boolean[][] pawnsBoard = new Boolean[WIDTH][HEIGHT];

    public DefaultSettings() {
        createBoard();
        addPawn();
    }

    public boolean[][] getFieldsBoard() {
        return fieldsBoard;
    }

    public Boolean[][] getPawnsBoard() {
        return pawnsBoard;
    }

    public Field[][] getLogicBoard() {
        return CopyBoard.invoke(logicBoard);
    }

    public void createBoard() {
        for (int x = 0; x < HEIGHT; x++) {
            for (int y = 0; y < WIDTH; y++) {
                Field field = new Field((x + y) % 2 != 0);
                logicBoard[x][y] = field;
                fieldsBoard[x][y] = (x + y) % 2 != 0;
            }
        }
    }

    public void addPawn() {
        Pawn pawn = null;
        for (int x = 0; x < logicBoard.length; x++) {
            for (int y = 0; y < logicBoard[x].length; y++) {
                if (logicBoard[x][y].isBlack() && y <= 2) {
                    pawn = new Pawn(true, x, y);
                    logicBoard[x][y].setPawn(pawn);
                    pawnsBoard[x][y] = true;
                } else if (logicBoard[x][y].isBlack() && y >= 5) {
                    pawn = new Pawn(false, x, y);
                    logicBoard[x][y].setPawn(pawn);
                    pawnsBoard[x][y] = false;
                } else {
                    pawnsBoard[x][y] = null;
                }
            }
        }
    }
}

