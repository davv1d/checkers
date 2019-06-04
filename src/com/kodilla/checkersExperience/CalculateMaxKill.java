package com.kodilla.checkersExperience;

import com.kodilla.myEnum.PawnType;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class CalculateMaxKill {


    public static List<PawnAndPositions> findMaxKillAmongAll(List<PawnAndPositions> allPawnsAndPositions, PawnType pawnType) {
        List<PawnAndPositions> pawnsWithBiggestKill = new ArrayList<>();
        List<PawnAndPositions> allPawnsOneType = findAllPawnsOneType(allPawnsAndPositions, pawnType);
        OptionalInt max = findNumberOfMaxKIll(allPawnsOneType);
        if (max.isPresent()) {
            int biggestKill = max.getAsInt();
            for (PawnAndPositions pawnAndPositions : allPawnsOneType) {
                if (pawnAndPositions.getAmountKill() == biggestKill) {
                    pawnsWithBiggestKill.add(pawnAndPositions);
                }
            }
            return pawnsWithBiggestKill;
        } else {
            return allPawnsOneType;
        }
    }

    private static List<PawnAndPositions> findAllPawnsOneType(List<PawnAndPositions> allPawnsAndPositions, PawnType pawnType) {
        return allPawnsAndPositions.stream()
                .filter(pawnAndPositions -> pawnAndPositions.getPawnOwner().getPawnType().equals(pawnType))
                .collect(Collectors.toList());
    }



    private static OptionalInt findNumberOfMaxKIll(List<PawnAndPositions> allPawnsOneType) {
        return allPawnsOneType.stream()
                .filter(pawnAndPositions -> pawnAndPositions.getAmountKill() > 0)
                .mapToInt(PawnAndPositions::getAmountKill)
                .max();
    }
}
