package com.kodilla.model.stateOfTheGame;

import com.kodilla.model.constantly.Win;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Game {
    private Field[][] gameBoard;
    private List<Pawn> blackPawns = new ArrayList<>();
    private List<Pawn> whitePawns = new ArrayList<>();

    public Game(Field[][] gameBoard) {
        this.gameBoard = gameBoard;

    }

    public boolean isPawnsOfASpecificType(boolean isBlack) {
        return getPawnsFromBoard().stream()
                .noneMatch(pawn -> pawn.isBlack() == isBlack);
    }

    private List<Pawn> getPawnsFromBoard() {
        return Arrays.stream(gameBoard)
                .flatMap(Arrays::stream)
                .filter(Field::hasPawn)
                .map(Field::getPawn)
                .collect(Collectors.toList());
    }

    public Field[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Field[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void setPawn(int x, int y, Pawn pawn) {
        gameBoard[x][y].setPawn(pawn);
    }

    public Pawn findPawn(int lastPositionX, int lastPositionY) {
        for (Field[] fields : gameBoard) {
            for (Field field : fields) {
                if (field.hasPawn() &&
                        field.getPawn().getLastPositionX() == lastPositionX &&
                        field.getPawn().getLastPositionY() == lastPositionY) {
                    return field.getPawn();
                }
            }
        }
        return null;
    }
}
