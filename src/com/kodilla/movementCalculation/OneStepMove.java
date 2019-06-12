package com.kodilla.movementCalculation;

import com.kodilla.constantly.KindOfPosition;
import com.kodilla.oldElements.Position;
import com.kodilla.oldElements.Pawn;

public class OneStepMove {

    private Position position;
    private Pawn compactedPawn;
    private KindOfPosition kindOfPosition;
    private int moveNumber;

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public OneStepMove(Position position, Pawn compactedPawn, KindOfPosition kindOfPosition) {
        this.position = position;
        this.compactedPawn = compactedPawn;
        this.kindOfPosition = kindOfPosition;
    }

    public KindOfPosition getKindOfPosition() {
        return kindOfPosition;
    }

    public Position getPosition() {
        return position;
    }

    public Pawn getCompactedPawn() {
        return compactedPawn;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean hasCompactedPawn(){
        return compactedPawn != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OneStepMove that = (OneStepMove) o;

        if (getPosition() != null ? !getPosition().equals(that.getPosition()) : that.getPosition() != null)
            return false;
        if (getCompactedPawn() != null ? !getCompactedPawn().equals(that.getCompactedPawn()) : that.getCompactedPawn() != null)
            return false;
        return getKindOfPosition() == that.getKindOfPosition();

    }

    @Override
    public int hashCode() {
        int result = getPosition() != null ? getPosition().hashCode() : 0;
        result = 31 * result + (getCompactedPawn() != null ? getCompactedPawn().hashCode() : 0);
        result = 31 * result + (getKindOfPosition() != null ? getKindOfPosition().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PosKil{" +
                "pos=" + position +
                ", kill=" + compactedPawn +
                '}';
    }
}
