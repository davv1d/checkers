package com.kodilla.checkersExperience;

import com.kodilla.myEnum.KindOfPosition;
import com.kodilla.testFindallMoves.Position;
import com.kodilla.view.Pawn;

public class PositionAndKilledPawn {
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

    public PositionAndKilledPawn(Position position, Pawn compactedPawn, KindOfPosition kindOfPosition) {
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

        PositionAndKilledPawn that = (PositionAndKilledPawn) o;

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
        return "PositionAndKilledPawn{" +
                "position=" + position +
                ", compactedPawn=" + compactedPawn +
                ", kindOfPosition=" + kindOfPosition +
                '}';
    }
}
