package com.kodilla.movementCalculation;

import com.kodilla.oldElements.Position;
import com.kodilla.oldElements.Pawn;

import java.util.ArrayList;
import java.util.List;

public class MoveOfPawn {
    private Pawn pawnOwner;
    private List<OneStepMove> positions = new ArrayList<>();

    public MoveOfPawn(Pawn pawnOwner) {
        this.pawnOwner = pawnOwner;
    }

    public MoveOfPawn(Pawn pawnOwner, List<OneStepMove> positions) {
        this.pawnOwner = pawnOwner;
        this.positions = positions;
    }

    public List<OneStepMove> getPositions() {
        return positions;
    }

    public Pawn getPawnOwner() {
        return pawnOwner;
    }

    public void addPosition(OneStepMove oneStepMove) {
        positions.add(oneStepMove);
    }

    public int getAmountKill() {
        return findAllCompactedPawns();
    }

    public List<Pawn> getAllCompactedPawns() {
        List<Pawn> compactedPawns = new ArrayList<>();
        for (OneStepMove oneStepMove : positions) {
            if (oneStepMove.getCompactedPawn() != null) {
                compactedPawns.add(oneStepMove.getCompactedPawn());
            }
        }
        return compactedPawns;
    }

    public Position getLastPosition() {
        return positions.get(positions.size() - 1).getPosition();
    }

    private int findAllCompactedPawns() {
        return (int) positions.stream()
                .filter(OneStepMove::hasCompactedPawn)
                .count();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoveOfPawn that = (MoveOfPawn) o;

        if (getPawnOwner() != null ? !getPawnOwner().equals(that.getPawnOwner()) : that.getPawnOwner() != null)
            return false;
        return getPositions() != null ? getPositions().equals(that.getPositions()) : that.getPositions() == null;

    }

    @Override
    public int hashCode() {
        int result = getPawnOwner() != null ? getPawnOwner().hashCode() : 0;
        result = 31 * result + (getPositions() != null ? getPositions().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "pawnOwner=" + pawnOwner +
                ", positions=" + positions +
                '}';
    }
}
