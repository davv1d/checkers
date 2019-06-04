package com.kodilla.checkersExperience;

import com.kodilla.myEnum.KindOfPosition;
import com.kodilla.tree.Node;
import com.kodilla.view.Pawn;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class CreatePathFromTheTree {

    public static void createPath(Pawn pawn, List<Node<PositionAndKilledPawn>> leafNodes, List<PawnAndPositions> test) {
        List<List<PositionAndKilledPawn>> list = new ArrayList<>();
        for (int i = 0; i < leafNodes.size(); i++) {
            Node<PositionAndKilledPawn> node = leafNodes.get(i);
            list.add(new ArrayList<>());
            while (node != null) {
                list.get(i).add(node.getDate());
                node = node.getParent();
            }
        }
        List<Integer> maxKillCount = new ArrayList<>();
        OptionalInt max = findMaxKill(list, maxKillCount);
        createPawnAndPositions(pawn, test, list, max, maxKillCount);
    }

    private static OptionalInt findMaxKill(List<List<PositionAndKilledPawn>> list, List<Integer> maxKillCount) {
        for (int i = 0; i < list.size(); i++) {
            int count = 0;
            for (int j = 0; j < list.get(i).size(); j++) {
                if (list.get(i).get(j).hasCompactedPawn()) {
                    count++;
                }
            }
            maxKillCount.add(count);
        }
        return maxKillCount.stream()
                .mapToInt(value -> value)
                .max();
    }

    private static void createPawnAndPositions(Pawn pawn, List<PawnAndPositions> test, List<List<PositionAndKilledPawn>> list, OptionalInt biggestKill, List<Integer> maxKillCount) {
        if (biggestKill.isPresent()) {
            for (int j = 0; j < list.size(); j++) {
                if (maxKillCount.get(j) == biggestKill.getAsInt()) {
                    int number = 0;
                    PawnAndPositions pawnAndPositions = new PawnAndPositions(pawn);
                    for (int k = list.get(j).size() - 1; k >= 0; k--) {
                        if (list.get(j).get(k).getKindOfPosition().equals(KindOfPosition.NONE) ||
                                list.get(j).get(k).getKindOfPosition().equals(KindOfPosition.SIMPLE_MOVE)) {
                            PositionAndKilledPawn p = list.get(j).get(k);
                            p.setMoveNumber(number);
                            pawnAndPositions.addPosition(p);
                            number++;
                        }
                    }
                    test.add(pawnAndPositions);
                }
            }
        }
    }
}
