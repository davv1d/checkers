package com.kodilla.model.boardBeaviour;

import com.kodilla.model.dataObject.MoveData;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;
import com.kodilla.model.elementsOfTheBoard.Position;

import java.util.List;

public class ActionsOnThePawns {

    public static void removingKilledPawns(List<Position> pawnToBeRemoved, Field[][] board) {
        for (Position position : pawnToBeRemoved) {
            board[position.getX()][position.getY()].setPawn(null);
        }
    }

    public static void doQueen(Pawn pawn, boolean doQueen) {
        if (doQueen) {
            pawn.setQueen(true);
        }
    }

    public static void realMove(MoveData moveData, Pawn pawn, Field[][] board) {
        pawn.setLastPositionX(moveData.getNextX());
        pawn.setLastPositionY(moveData.getNextY());
        board[moveData.getX()][moveData.getY()].setPawn(null);
        board[moveData.getNextX()][moveData.getNextY()].setPawn(pawn);
    }
}
