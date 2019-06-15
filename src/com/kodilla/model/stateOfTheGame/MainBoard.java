package com.kodilla.model.stateOfTheGame;

import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MainBoard {
    private Field[][] gameBoard;

    public MainBoard(Field[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Field[][] getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(Field[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

}
