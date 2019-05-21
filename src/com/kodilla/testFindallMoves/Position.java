package com.kodilla.testFindallMoves;

import com.kodilla.myEnum.KindOfTempQueenMove;

public class Position {
    private int moveNumber;
    private int x, y;
    private KindOfTempQueenMove kind;


    public Position(int x, int y) {
        this.x = x;
        this.y = y;
        this.moveNumber = 0;
        this.kind = KindOfTempQueenMove.NONE;
    }

    public Position(int moveNumber, int x, int y) {
        this.moveNumber = moveNumber;
        this.x = x;
        this.y = y;
        this.kind = KindOfTempQueenMove.NONE;
    }

    public Position(int x, int y, KindOfTempQueenMove kind) {
        this.x = x;
        this.y = y;
        this.kind = kind;
    }

    public Position(int moveNumber, int x, int y, KindOfTempQueenMove kind) {
        this.moveNumber = moveNumber;
        this.x = x;
        this.y = y;
        this.kind = kind;
    }

    public void setKind(KindOfTempQueenMove kind) {
        this.kind = kind;
    }

    public KindOfTempQueenMove getKind() {
        return kind;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (getMoveNumber() != position.getMoveNumber()) return false;
        if (getX() != position.getX()) return false;
        if (getY() != position.getY()) return false;
        return getKind() == position.getKind();

    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        result = 31 * result + (getKind() != null ? getKind().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Position{" +
                "moveNumber=" + moveNumber +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

}
