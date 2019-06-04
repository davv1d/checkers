package com.kodilla.computer;

import com.kodilla.checkersExperience.PawnAndPositions;
import com.kodilla.testFindallMoves.Position;
import com.kodilla.view.Field;

public class MoveBoardAndPoint {
    private PawnAndPositions pawnAndPositions;
    private Field[][] board;
    private int point;
    private Position position;

    public MoveBoardAndPoint(PawnAndPositions pawnAndPositions, Field[][] board, int point, Position position) {
        this.pawnAndPositions = pawnAndPositions;
        this.board = board;
        this.point = point;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public PawnAndPositions getPawnAndPositions() {
        return pawnAndPositions;
    }

    public Field[][] getBoard() {
        return board;
    }

    public int getPoint() {
        return point;
    }

    public void setPawnAndPositions(PawnAndPositions pawnAndPositions) {
        this.pawnAndPositions = pawnAndPositions;
    }

    public void setBoard(Field[][] board) {
        this.board = board;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
