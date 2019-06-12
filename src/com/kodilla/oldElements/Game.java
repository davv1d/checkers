package com.kodilla.oldElements;

import com.kodilla.playerMove.MovingThePiece;
import com.kodilla.computerTest.Computer;
import com.kodilla.view.CheckersApp;

public class Game {

    private MovingThePiece movingThePiece;
    private Computer computer = new Computer();
    private CheckersApp view;
    public Game(CheckersApp view) {
        this.view = view;
        this.movingThePiece = new MovingThePiece(view);
    }

    public void tryMove(int x, int y, Pawn pawn, Field[][] board) {
        movingThePiece.checkingTheCorrectnessOfTheMovement(x,y,pawn,board);
        System.out.println(movingThePiece.isEndOfRound());
        if(movingThePiece.isEndOfRound()){
            computer.test(board);
        }
        System.out.println("board after computerTest");
        computer.printAllBoard(board);
    }
}
