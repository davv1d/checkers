package com.kodilla.testFindallMoves;

import com.kodilla.myEnum.KindOfMove;
import com.kodilla.view.Pawn;

import java.util.LinkedHashMap;
import java.util.Map;

public class Movement {
    private KindOfMove kindOfMove;
    private Map<Position, Pawn> nextPositions = new LinkedHashMap<>();

    public Movement(KindOfMove kindOfMove) {
        this.kindOfMove = kindOfMove;
    }


    public KindOfMove getKindOfMove() {
        return kindOfMove;
    }


    public int getAmountOfKill() {
        if (kindOfMove.equals(KindOfMove.PAWN_KILL)) {
            return nextPositions.size();
        } else if (kindOfMove.equals(KindOfMove.QUEEN_KILL)) {
            return countTheNumberOfKillings();
        } else {
            return 0;
        }
    }


    public void addNextPositions(Position position, Pawn pawn) {
        nextPositions.put(position, pawn);
    }

    private int countTheNumberOfKillings() {
        return (int) nextPositions.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .count();
    }

    public Map<Position, Pawn> getNextPositions() {
        return nextPositions;
    }

    @Override
    public String toString() {
        return "Movement{" +
                "nextPositions=" + nextPositions +
                '}';
    }
}
