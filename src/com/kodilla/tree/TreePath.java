package com.kodilla.tree;

import com.kodilla.movementCalculation.MoveOfPawn;
import com.kodilla.movementCalculation.OneStepMove;
import com.kodilla.constantly.KindOfPosition;
import com.kodilla.oldElements.Pawn;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class TreePath {

    public static void createPath(Pawn pawn, List<Node<OneStepMove>> leafNodes, List<MoveOfPawn> movesOfPawns) {
        List<List<OneStepMove>> list = new ArrayList<>();
        for (int i = 0; i < leafNodes.size(); i++) {
            Node<OneStepMove> node = leafNodes.get(i);
            list.add(new ArrayList<>());
            while (node != null) {
                list.get(i).add(node.getDate());
                node = node.getParent();
            }
        }
        List<Integer> maxKillCount = new ArrayList<>();
        OptionalInt max = findMaxKill(list, maxKillCount);
        createPawnMove(pawn, movesOfPawns, list, max, maxKillCount);
    }

    private static OptionalInt findMaxKill(List<List<OneStepMove>> list, List<Integer> maxKillCount) {
        for (List<OneStepMove> oneStepMoves : list) {
            int count = 0;
            for (OneStepMove oneStepMove : oneStepMoves) {
                if (oneStepMove.hasCompactedPawn()) {
                    count++;
                }
            }
            maxKillCount.add(count);
        }
        return maxKillCount.stream()
                .mapToInt(value -> value)
                .max();
    }

    private static void createPawnMove(Pawn pawn, List<MoveOfPawn> test, List<List<OneStepMove>> list, OptionalInt biggestKill, List<Integer> maxKillCount) {
        if (biggestKill.isPresent()) {
            for (int j = 0; j < list.size(); j++) {
                if (maxKillCount.get(j) == biggestKill.getAsInt()) {
                    int number = 0;
                    MoveOfPawn moveOfPawn = new MoveOfPawn(pawn);
                    for (int k = list.get(j).size() - 1; k >= 0; k--) {
                        if (list.get(j).get(k).getKindOfPosition().equals(KindOfPosition.NONE) ||
                                list.get(j).get(k).getKindOfPosition().equals(KindOfPosition.SIMPLE_MOVE)) {
                            OneStepMove p = list.get(j).get(k);
                            p.setMoveNumber(number);
                            moveOfPawn.addPosition(p);
                            number++;
                        }
                    }
                    test.add(moveOfPawn);
                }
            }
        }
    }
}
