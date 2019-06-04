package com.kodilla.checkersExperience;

import com.kodilla.testFindallMoves.Position;
import com.kodilla.view.Pawn;

import java.util.ArrayList;
import java.util.List;

public class PawnAndPositions {
    private Pawn pawnOwner;
    private List<PositionAndKilledPawn> positions = new ArrayList<>();

    public PawnAndPositions(Pawn pawnOwner) {
        this.pawnOwner = pawnOwner;
    }

    public List<PositionAndKilledPawn> getPositions() {
        return positions;
    }

    public Pawn getPawnOwner() {
        return pawnOwner;
    }

    public void setPawnOwner(Pawn pawnOwner) {
        this.pawnOwner = pawnOwner;
    }

    public void addPosition(PositionAndKilledPawn positionAndKilledPawn) {
        positions.add(positionAndKilledPawn);
    }

    public int getAmountKill() {
        return findAllCompactedPawns();
    }

    public List<Pawn> getAllCompactedPawns() {
        List<Pawn> compactedPawns = new ArrayList<>();
        for (PositionAndKilledPawn positionAndKilledPawn : positions) {
            if (positionAndKilledPawn.getCompactedPawn() != null) {
                compactedPawns.add(positionAndKilledPawn.getCompactedPawn());
            }
        }
        return compactedPawns;
    }

    public Position getLastPosition() {
        return positions.get(positions.size() - 1).getPosition();
    }

    private int findAllCompactedPawns() {
        return (int) positions.stream()
                .filter(PositionAndKilledPawn::hasCompactedPawn)
                .count();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PawnAndPositions that = (PawnAndPositions) o;

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
}
