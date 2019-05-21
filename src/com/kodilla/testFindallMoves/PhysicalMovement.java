package com.kodilla.testFindallMoves;

import com.kodilla.view.Field;
import com.kodilla.view.Pawn;

import java.util.List;
import java.util.Map;

public class PhysicalMovement {

    public static void removeCompactedPawns(List<Map<Position, Pawn>> removeMap, Map<Pawn, List<Movement>> possibleMoves, Field[][] board) {
        for (Map<Position, Pawn> positionPawnMap : removeMap) {
            for (Map.Entry<Position, Pawn> entry : positionPawnMap.entrySet()) {
                if (entry.getValue() != null) {
                    possibleMoves.remove(entry.getValue());
                    if (board[entry.getValue().getLastPositionX()][entry.getValue().getLastPositionY()].getPawn() != null) {
                        board[entry.getValue().getLastPositionX()][entry.getValue().getLastPositionY()].getPawn().setVisible(false);
                        board[entry.getValue().getLastPositionX()][entry.getValue().getLastPositionY()].setPawn(null);
                    }
                }
            }
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
