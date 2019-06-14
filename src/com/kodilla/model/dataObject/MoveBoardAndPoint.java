package com.kodilla.model.dataObject;

import com.kodilla.model.movementCalculation.MoveOfPawn;
import com.kodilla.model.elementsOfTheBoard.Position;
import com.kodilla.model.elementsOfTheBoard.Field;

public class MoveBoardAndPoint {
    private MoveOfPawn moveOfPawn;
    private Field[][] board;
    private int point;
    private Position position;

    public MoveBoardAndPoint(MoveOfPawn moveOfPawn, Field[][] board, int point, Position position) {
        this.moveOfPawn = moveOfPawn;
        this.board = board;
        this.point = point;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public MoveOfPawn getMoveOfPawn() {
        return moveOfPawn;
    }

    public Field[][] getBoard() {
        return board;
    }

    public int getPoint() {
        return point;
    }

    public void setMoveOfPawn(MoveOfPawn moveOfPawn) {
        this.moveOfPawn = moveOfPawn;
    }

    public void setBoard(Field[][] board) {
        this.board = board;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
