package com.kodilla.testFindallMoves;

import com.kodilla.myEnum.PawnType;
import com.kodilla.view.CheckersApp;
import com.kodilla.view.Field;
import com.kodilla.view.Pawn;

import java.util.List;

public class PhysicalMovement {
    private static CheckersApp view;

    public PhysicalMovement(CheckersApp view) {
        PhysicalMovement.view = view;
    }


    public static void removeCompactedPawns(List<Pawn> compactedPawns, Field[][] board) {
        for (Pawn pawn : compactedPawns) {
            if (pawn != null) {
                if (board[pawn.getLastPositionX()][pawn.getLastPositionY()].getPawn() != null) {
                    board[pawn.getLastPositionX()][pawn.getLastPositionY()].getPawn().setVisible(false);
                    board[pawn.getLastPositionX()][pawn.getLastPositionY()].setPawn(null);
                }
            }
        }
    }

    public void doQueen(int nextY, Pawn pawn) {
        if (!pawn.isQueen() && (pawn.getPawnType().equals(PawnType.BLACK) && nextY == 7) ||
                (pawn.getPawnType().equals(PawnType.WHITE) && nextY == 0)) {
            pawn.setQueen(true);
            view.changePawnIntoAQueen(pawn);
        }
    }


    public static void pawnMove(int nextX, int nextY, Pawn pawn, Field[][] board) {
        board[pawn.getLastPositionX()][pawn.getLastPositionY()].setPawn(null);
        board[nextX][nextY].setPawn(pawn);
        pawn.move(nextX, nextY);
    }

    public static void backPawnToLastPosition(Pawn pawn) {
        pawn.move(pawn.getLastPositionX(), pawn.getLastPositionY());
    }
}
