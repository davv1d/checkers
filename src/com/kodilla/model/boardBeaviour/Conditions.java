package com.kodilla.model.boardBeaviour;

import com.kodilla.model.dataObject.ItemsOfBoard;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;
import com.kodilla.model.movementCalculation.Coordinates;

import java.util.List;

public class Conditions {

    public static boolean isTwoPawnsNextToYourself(Coordinates coor, Field[][] board) {
        return board[coor.getX()][coor.getY()].hasPawn() && board[coor.getNextX()][coor.getNextY()].hasPawn();
    }

    public static boolean isTheSameKindOfPawn(Coordinates coor, Field[][] board, Pawn pawn) {
        return board[coor.getX()][coor.getY()].hasPawn() && board[coor.getX()][coor.getY()].getPawn().isBlack() == pawn.isBlack();
    }

    public static boolean isKill(Coordinates coor, Field[][] board, Pawn pawn) {
        return board[coor.getX()][coor.getY()].hasPawn() &&
                !board[coor.getX()][coor.getY()].getPawn().isBlack() == pawn.isBlack() &&
                !board[coor.getNextX()][coor.getNextY()].hasPawn();
    }

    public static boolean doQueen(Pawn pawn, int nextY, boolean endOfRound) {
        return !pawn.isQueen() && endOfRound &&
                (pawn.isBlack() && nextY == 7) ||
                (!pawn.isBlack() && nextY == 0);
    }


    public static boolean isRealKllAllCase(Coordinates coor, ItemsOfBoard items, List<Pawn> compactedPawns) {
        return items.getBoard()[coor.getX()][coor.getY()].hasPawn() &&
                !items.getBoard()[coor.getX()][coor.getY()].getPawn().isBlack() == items.getPawn().isBlack() &&
                !items.getBoard()[coor.getNextX()][coor.getNextY()].hasPawn() &&
                !compactedPawns.contains(items.getBoard()[coor.getX()][coor.getY()].getPawn());
    }

    public static boolean pawnIsKilled(Coordinates coor, Field[][] board , List<Pawn> compactedPawns) {
        return board[coor.getX()][coor.getY()].hasPawn() && compactedPawns.contains(board[coor.getX()][coor.getY()].getPawn());
    }
}
