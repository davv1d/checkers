package com.kodilla.model.stateOfTheGame;

import com.kodilla.model.constantly.Winner;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.movementCalculation.calculateAll.Calculate;

public class Judge {

    public static Winner checkWin(boolean isBlack, Field[][] board) {
        boolean isOpponentNoMove;
        if (isBlack) {
            isOpponentNoMove = Calculate.getMaxMovesAmongWhite(board).isEmpty();
        } else {
            isOpponentNoMove = Calculate.getMaxMovesAmongBlack(board).isEmpty();
        }
        return chooseAWinner(isOpponentNoMove, isBlack);
    }

    private static Winner chooseAWinner(boolean isOpponentNoMove, boolean isBlack) {
        if (isOpponentNoMove && isBlack) {
            return Winner.BLACK;
        } else if (isOpponentNoMove) {
            return Winner.WHITE;
        } else {
            return Winner.NONE;
        }
    }
}
