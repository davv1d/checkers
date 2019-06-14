package com.kodilla.model.movementCalculation.calculateAll;

import com.kodilla.model.movementCalculation.MoveOfPawn;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class CalculateMaxKill {


    public static List<MoveOfPawn> findMaxKillAmongAll(List<MoveOfPawn> allPawnsAndPositions, boolean pawnIsBlack) {
        List<MoveOfPawn> pawnsWithBiggestKill = new ArrayList<>();
        List<MoveOfPawn> allPawnsOneType = findAllPawnsOneType(allPawnsAndPositions, pawnIsBlack);
        OptionalInt max = findNumberOfMaxKIll(allPawnsOneType);
        if (max.isPresent()) {
            int biggestKill = max.getAsInt();
            for (MoveOfPawn moveOfPawn : allPawnsOneType) {
                if (moveOfPawn.getAmountKill() == biggestKill) {
                    pawnsWithBiggestKill.add(moveOfPawn);
                }
            }
            return pawnsWithBiggestKill;
        } else {
            return allPawnsOneType;
        }
    }

    private static List<MoveOfPawn> findAllPawnsOneType(List<MoveOfPawn> allPawnsAndPositions, boolean pawnIsBlack) {
        return allPawnsAndPositions.stream()
                .filter(pawnAndPositions -> pawnAndPositions.getPawnOwner().isBlack() == pawnIsBlack)
                .collect(Collectors.toList());
    }



    private static OptionalInt findNumberOfMaxKIll(List<MoveOfPawn> allPawnsOneType) {
        return allPawnsOneType.stream()
                .filter(pawnAndPositions -> pawnAndPositions.getAmountKill() > 0)
                .mapToInt(MoveOfPawn::getAmountKill)
                .max();
    }
}
