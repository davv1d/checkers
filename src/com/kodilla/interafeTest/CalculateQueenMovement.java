package com.kodilla.interafeTest;

import com.kodilla.testFindallMoves.Movement;
import com.kodilla.testFindallMoves.Position;
import com.kodilla.view.Field;
import com.kodilla.myEnum.KindOfMove;
import com.kodilla.myEnum.KindOfTempQueenMove;
import com.kodilla.view.Pawn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CalculateQueenMovement {

    private Position positionQueenEnd = new Position(8, 8, KindOfTempQueenMove.BACK);
    private Field[][] board;

    public CalculateQueenMovement(Field[][] board) {
        this.board = board;
    }

    public List<Movement> getAllPossiblePositions(List<Position> temporaryQueenMovesList, Pawn pawn) {
        return isKillInQueenMovements(temporaryQueenMovesList, pawn);
    }

    private List<Movement> isKillInQueenMovements(List<Position> temporaryQueenMovesList, Pawn pawn) {
        boolean isKill = temporaryQueenMovesList.stream()
                .anyMatch(queenTemporaryPosition -> queenTemporaryPosition.getKind().equals(KindOfTempQueenMove.KILL_IN_LINE));
        if (isKill) {
            List<Position> withoutSimpleMove = temporaryQueenMovesList.stream()
                    .filter(queenTemporaryPosition -> !queenTemporaryPosition.getKind().equals(KindOfTempQueenMove.SIMPLE_MOVE))
                    .collect(Collectors.toList());
            List<List<Position>> allQueenMoves = divTemporaryMovesQueen(withoutSimpleMove);
            return calculateBiggestKill(allQueenMoves, pawn);
        } else {
            List<Movement> movements = new ArrayList<>();
            Movement queenSimpleMoves = new Movement(KindOfMove.MOVE_QUEEN);
            for (int i = 0; i < temporaryQueenMovesList.size(); i++) {
                queenSimpleMoves.addNextPositions(temporaryQueenMovesList.get(i), null);
            }
            movements.add(queenSimpleMoves);
            return movements;
        }
    }

    private List<List<Position>> divTemporaryMovesQueen(List<Position> queenTempMoves) {
        int numberOfList = 0;
        int index = 0;
        int numberOfBack;
        List<List<Position>> allQueenMoves = new ArrayList<>();
        allQueenMoves.add(new ArrayList<>());
        while (index < queenTempMoves.size()) {
            numberOfBack = 0;
            while (!queenTempMoves.get(index).equals(positionQueenEnd)) {
                allQueenMoves.get(numberOfList).add(queenTempMoves.get(index));
                index++;
            }
            while (queenTempMoves.get(index).equals(positionQueenEnd)) {
                numberOfBack++;
                index++;
                if (index == queenTempMoves.size())
                    break;
            }
            if (index < queenTempMoves.size()) {
                back(numberOfList, numberOfBack, allQueenMoves);
                numberOfList++;
            }
        }
        return allQueenMoves;
    }

    private void back(int numberOfList, int numberOfBack, List<List<Position>> allQueenMoves) {
        allQueenMoves.add(new ArrayList<>());
        int i = allQueenMoves.get(numberOfList).size() - 1;
        while (numberOfBack > 0) {
            if (allQueenMoves.get(numberOfList).get(i).getKind().equals(KindOfTempQueenMove.KILL_IN_LINE) ||
                    allQueenMoves.get(numberOfList).get(i).getKind().equals(KindOfTempQueenMove.KILL_DIAGONALLY)) {
                numberOfBack--;
            }
            i--;
        }
        for (int j = 0; j < i + 1; j++) {
            allQueenMoves.get(numberOfList + 1).add(allQueenMoves.get(numberOfList).get(j));
        }
    }

    private List<Movement> calculateBiggestKill(List<List<Position>> allQueenMoves, Pawn pawn) {
        int count = 0;
        List<Integer> numberOfKill = new ArrayList<>();
        for (int i = 0; i < allQueenMoves.size(); i++) {
            for (int j = 0; j < allQueenMoves.get(i).size(); j++) {
                if (allQueenMoves.get(i).get(j).getKind().equals(KindOfTempQueenMove.KILL_DIAGONALLY) ||
                        allQueenMoves.get(i).get(j).getKind().equals(KindOfTempQueenMove.KILL_IN_LINE)) {
                    count++;
                }
            }
            numberOfKill.add(count);
            count = 0;
        }
        return findMovementWithMaxKill(numberOfKill, allQueenMoves, pawn);
    }

    private List<Movement> findMovementWithMaxKill(List<Integer> numberOfKill, List<List<Position>> allQueenMoves, Pawn pawn) {
        List<List<Position>> maxKillsQueen = new ArrayList<>();
        if (!numberOfKill.isEmpty()) {
            Integer max = Collections.max(numberOfKill);
            int index = 0;
            while (index < numberOfKill.size()) {
                if (numberOfKill.get(index) == max) {
                    maxKillsQueen.add(allQueenMoves.get(index));
                }
                index++;
            }
        }
        return deleteUnnecessaryPositions(maxKillsQueen, pawn);
    }

    private List<Movement> deleteUnnecessaryPositions(List<List<Position>> maxKillsQueen, Pawn pawn) {
        boolean isNoKillInLine;
        int index = 0;
        List<List<Position>> removedPositions = new ArrayList<>();
        for (int i = 0; i < maxKillsQueen.size(); i++) {
            isNoKillInLine = true;
            removedPositions.add(new ArrayList<>());
            for (int j = 0; j < maxKillsQueen.get(i).size(); j++) {
                if (isNoKillInLine && (maxKillsQueen.get(i).get(j).getKind().equals(KindOfTempQueenMove.KILL_IN_LINE) ||
                        maxKillsQueen.get(i).get(j).getKind().equals(KindOfTempQueenMove.KILL_DIAGONALLY))) {
                    isNoKillInLine = false;
                    index = j;
                } else if (!isNoKillInLine && maxKillsQueen.get(i).get(j).getKind().equals(KindOfTempQueenMove.KILL_DIAGONALLY)) {
                    for (int k = index + 1; k < j - 1; k++) {
                        removedPositions.get(i).add(maxKillsQueen.get(i).get(k));
                    }
                    isNoKillInLine = true;
                    j--;
                }
            }
        }
        for (int i = 0; i < maxKillsQueen.size(); i++) {
            maxKillsQueen.get(i).removeAll(removedPositions.get(i));
        }
        return addMoveNumberToEveryPosition(maxKillsQueen, pawn);
    }

    private List<Movement> addMoveNumberToEveryPosition(List<List<Position>> maxKillsQueen, Pawn pawn) {
        boolean isNoKill;
        int index = 0;
        int numberOfMove;
        List<List<Position>> positionsList = new ArrayList<>();
        for (int i = 0; i < maxKillsQueen.size(); i++) {
            isNoKill = true;
            numberOfMove = 0;
            positionsList.add(new ArrayList<>());
            for (int j = 0; j < maxKillsQueen.get(i).size(); j++) {
                if (isNoKill && maxKillsQueen.get(i).get(j).getKind().equals(KindOfTempQueenMove.KILL_IN_LINE) ||
                        isNoKill && maxKillsQueen.get(i).get(j).getKind().equals(KindOfTempQueenMove.KILL_DIAGONALLY)) {
                    isNoKill = false;
                    index = j;
                } else if (!isNoKill && (maxKillsQueen.get(i).get(j).getKind().equals(KindOfTempQueenMove.KILL_IN_LINE) ||
                        maxKillsQueen.get(i).get(j).getKind().equals(KindOfTempQueenMove.KILL_DIAGONALLY))) {
                    addMoveNumberToPositionAndPositionToList(index, numberOfMove, positionsList, i, j, maxKillsQueen);
                    numberOfMove++;
                    isNoKill = true;
                    j--;
                } else if (!isNoKill && j == maxKillsQueen.get(i).size() - 1) {
                    addMoveNumberToPositionAndPositionToList(index, numberOfMove, positionsList, i, j + 1, maxKillsQueen);
                }
            }
        }
        return crateMovements(maxKillsQueen, pawn);
    }

    private List<Movement> crateMovements(List<List<Position>> maxKillsQueen, Pawn pawn) {
        Movement movement;
        List<Movement> movements = new ArrayList<>();
        for (int i = 0; i < maxKillsQueen.size(); i++) {
            movement = new Movement(KindOfMove.QUEEN_KILL);
            for (int j = 0; j < maxKillsQueen.get(i).size(); j++) {
                Position position = maxKillsQueen.get(i).get(j);
                Pawn pawn1;
                if (board[position.getX()][position.getY()].hasPawn() && board[position.getX()][position.getY()].getPawn().equals(pawn)) {
                    pawn1 = null;
                } else {
                    pawn1 = board[position.getX()][position.getY()].getPawn();
                }
                movement.addNextPositions(position, pawn1);
            }
            movements.add(movement);
        }
        return movements;
    }

    private void addMoveNumberToPositionAndPositionToList(int index, int numberOfMove, List<List<Position>> positionsList, int i, int j, List<List<Position>> maxKillsQueen) {
        for (int k = index; k < j; k++) {
            Position position = maxKillsQueen.get(i).get(k);
            position.setMoveNumber(numberOfMove);
            positionsList.get(i).add(position);
        }
    }
}
