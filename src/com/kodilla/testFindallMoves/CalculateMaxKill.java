package com.kodilla.testFindallMoves;

import com.kodilla.interafeTest.CalculateAllMovements;
import com.kodilla.myEnum.KindOfMove;
import com.kodilla.myEnum.PawnType;
import com.kodilla.view.*;

import java.util.*;
import java.util.stream.Collectors;

public class CalculateMaxKill {

    private CalculateAllMovements calculateAllMovements;


    public CalculateMaxKill(CalculateAllMovements calculateAllMovements) {
        this.calculateAllMovements = calculateAllMovements;
    }

    public Map<Pawn, List<Movement>> getWhitePawnsWithMaxPossibility() {
        return findMaxKillAmongAll(PawnType.WHITE);
    }

    public Map<Pawn, List<Movement>> getBlackPawnsWithMaxPossibility() {
        return findMaxKillAmongAll(PawnType.BLACK);
    }

    private Map<Pawn, List<Movement>> findMaxKillAmongAll(PawnType pawnType) {
        Map<Pawn, List<Movement>> maxPossibleMoves = new HashMap<>();
        OptionalInt max = findNumberOfMaxKIll(pawnType);
        if (max.isPresent()) {
            int asInt = max.getAsInt();
            for (Map.Entry<Pawn, List<Movement>> entry : calculateAllMovements.getPossibleMoves().entrySet()) {
                if (entry.getKey().getPawnType().equals(pawnType)) {
                    List<Movement> maxPawnMovement = entry.getValue().stream()
                            .filter(movement -> (movement.getKindOfMove().equals(KindOfMove.PAWN_KILL) ||
                                    movement.getKindOfMove().equals(KindOfMove.QUEEN_KILL)) && (movement.getAmountOfKill() == asInt))
                            .collect(Collectors.toList());
                    if (maxPawnMovement.size() > 0)
                        maxPossibleMoves.put(entry.getKey(), maxPawnMovement);
                }
            }
        }
        return maxPossibleMoves;
    }

    private OptionalInt findNumberOfMaxKIll(PawnType pawnType) {
        return calculateAllMovements.getPossibleMoves().entrySet().stream()
                .filter(pawnListEntry -> pawnListEntry.getKey().getPawnType().equals(pawnType))
                .flatMap(pawnListEntry -> pawnListEntry.getValue().stream())
                .filter(movement -> movement.getKindOfMove().equals(KindOfMove.PAWN_KILL) || movement.getKindOfMove().equals(KindOfMove.QUEEN_KILL))
                .mapToInt(Movement::getAmountOfKill)
                .max();
    }
}
