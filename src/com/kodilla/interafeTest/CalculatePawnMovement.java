package com.kodilla.interafeTest;

import com.kodilla.myEnum.KindOfMove;
import com.kodilla.testFindallMoves.Movement;
import com.kodilla.testFindallMoves.Position;
import com.kodilla.view.Pawn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalculatePawnMovement {

    private Position auxiliaryPosition = new Position(0, 0);

    public List<Movement> addMovesToMap(List<Position> temporaryMovesList, Map<Position, Pawn> testMap) {
        List<Movement> allMovesOfThePawn = new ArrayList<>();
        if (temporaryMovesList.size() > 1) {
            List<List<Position>> divisionMoves = divisionList(temporaryMovesList);
            List<List<Position>> maxKills = findMaxKills(divisionMoves);
            Movement type;
            for (List<Position> listAllMove : maxKills) {
                type = new Movement(KindOfMove.PAWN_KILL);
                for (int j = 0; j < listAllMove.size(); j++) {
                    Position position = listAllMove.get(j);
                    Pawn compactedPawn = null;
                    if (testMap.containsKey(position)) {
                        compactedPawn = testMap.get(position);
                    }
                    position.setMoveNumber(j);
                    type.addNextPositions(position, compactedPawn);
                }
                allMovesOfThePawn.add(type);
            }
        }
        return allMovesOfThePawn;
    }

    private List<List<Position>> findMaxKills(List<List<Position>> divisionMoves) {
        int max = divisionMoves.stream()
                .mapToInt(List::size)
                .max().getAsInt();
        List<List<Position>> maxKills = divisionMoves.stream()
                .filter(positions -> positions.size() == max)
                .collect(Collectors.toList());
        return maxKills;
    }

    private List<List<Position>> divisionList(List<Position> temporaryMovesList) {
        int numberOfList = 0;
        List<List<Position>> dividedMoves = new ArrayList<>();
        dividedMoves.add(new ArrayList<>());
        int index = createNewList(numberOfList, 0, temporaryMovesList, dividedMoves);
        while (index < temporaryMovesList.size()) {
            int numberOfZero = 0;
            while (index < temporaryMovesList.size() && temporaryMovesList.get(index).equals(auxiliaryPosition)) {
                numberOfZero++;
                index++;
            }
            if (index < temporaryMovesList.size()) {
                dividedMoves.add(new ArrayList<>());
                copyValuesToListWithBeforeList(numberOfList, numberOfZero, dividedMoves);
                numberOfList++;
                index = createNewList(numberOfList, index, temporaryMovesList, dividedMoves);
            }
        }
        return dividedMoves;
    }

    private void copyValuesToListWithBeforeList(int numberOfList, int numberOf0,List<List<Position>> dividedMoves) {
        for (int i = 0; i < dividedMoves.get(numberOfList).size() - numberOf0; i++) {
            dividedMoves.get(numberOfList + 1).add(dividedMoves.get(numberOfList).get(i));
        }
    }

    private int createNewList(int numberOfList, int index, List<Position> temporaryMovesList, List<List<Position>> dividedMoves) {
        while (!temporaryMovesList.get(index).equals(auxiliaryPosition)) {
            dividedMoves.get(numberOfList).add(temporaryMovesList.get(index));
            index++;
        }
        return index;
    }
}
