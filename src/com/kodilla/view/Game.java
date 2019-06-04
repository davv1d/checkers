package com.kodilla.view;

import com.kodilla.checkersExperience.MovingThePiece;
import com.kodilla.computer.Computer;

public class Game {

    private Field[][] board;

    MovingThePiece movingThePiece;
    Computer computer = new Computer();
    public Game(Field[][] board, CheckersApp view) {
        this.board = board;
        this.movingThePiece = new MovingThePiece(view);
    }

    public void tryMove(int x, int y, Pawn pawn, Field[][] board) {
        movingThePiece.checkingTheCorrectnessOfTheMovement(x,y,pawn,board);
        System.out.println(movingThePiece.isEndOfRound());


        if(movingThePiece.isEndOfRound()){
            computer.test(board);
        }
//        List<PawnAndPositions> initialize1 = calculate.calculateAllMoves(board);

        //realMove.checkingTheCorrectnessOfTheMovement(x, y, pawn);
        // System.out.println(realMove.isEndOfRound());
        //   if (realMove.isEndOfRound()) {
        //       calculateAllMovements.search();
        //       computer.stepFirstSecondAttempt();
        //           computer.copyingOfTheBoard();
        //    }
        //    calculateAllMovements.search();
        //     List<PawnAndPositions> initialize = calculate.getAllMaxMoves(board);
    }
}
