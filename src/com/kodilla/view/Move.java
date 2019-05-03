package com.kodilla.view;

public class Move {
    private Field[][] board;

    public Move(Field[][] board) {
        this.board = board;
    }

    public void checkMove(int x, int y, Pawn pawn) {
        // If white field return to last position
        if ((x + y) % 2 != 0) {
            checkQueen(x, y, pawn);
        } else {
            pawn.relocate(pawn.getLastPositionX(), pawn.getLastPositionY());
        }
    }

    private void checkQueen(int x, int y, Pawn pawn) {
        if (pawn.isQueen()) {
            queenMove(x, y, pawn);
        } else {
            pawnMove(x, y, pawn);
        }
    }

    private void pawnMove(int x, int y, Pawn pawn) {
        pawn.move(x, y);
    }

    private void queenMove(int x, int y, Pawn pawn) {
    }
}
