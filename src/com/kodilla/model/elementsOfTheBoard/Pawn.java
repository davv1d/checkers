package com.kodilla.model.elementsOfTheBoard;

public class Pawn {
    private boolean isBlack;
    private boolean isQueen;
    private int lastPositionX, lastPositionY;

    public Pawn(boolean isBlack, int x, int y) {
        this.isBlack = isBlack;
        this.lastPositionX = x;
        this.lastPositionY = y;
        this.isQueen = false;
    }

    public boolean isBlack() {
        return isBlack;
    }

    public boolean isQueen() {
        return isQueen;
    }

    public int getLastPositionX() {
        return lastPositionX;
    }

    public int getLastPositionY() {
        return lastPositionY;
    }

    public void setQueen(boolean queen) {
        isQueen = queen;
    }

    public void setLastPositionX(int lastPositionX) {
        this.lastPositionX = lastPositionX;
    }

    public void setLastPositionY(int lastPositionY) {
        this.lastPositionY = lastPositionY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pawn pawn = (Pawn) o;

        if (isBlack() != pawn.isBlack()) return false;
        if (isQueen() != pawn.isQueen()) return false;
        if (getLastPositionX() != pawn.getLastPositionX()) return false;
        return getLastPositionY() == pawn.getLastPositionY();

    }

    @Override
    public int hashCode() {
        int result = (isBlack() ? 1 : 0);
        result = 31 * result + (isQueen() ? 1 : 0);
        result = 31 * result + getLastPositionX();
        result = 31 * result + getLastPositionY();
        return result;
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "isBlack=" + isBlack +
                ", isQueen=" + isQueen +
                ", lastPositionX=" + lastPositionX +
                ", lastPositionY=" + lastPositionY +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Pawn pawn = new Pawn(isBlack, lastPositionX, lastPositionY);
        pawn.setQueen(this.isQueen);
        return pawn;
    }
}
