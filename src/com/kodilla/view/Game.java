package com.kodilla.view;

public class Game {
    private boolean isPlayerRound;
    private Move move;
//    private Field[][] board;

    public Game(Field[][] board) {
        this.move = new Move(board);
//        this.board = board;
    }

    public void tryMove(int x, int y, Pawn pawn) {
        move.checkMove(x, y, pawn);
    }
}
