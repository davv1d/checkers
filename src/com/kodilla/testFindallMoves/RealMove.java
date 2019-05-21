package com.kodilla.testFindallMoves;

import com.kodilla.interafeTest.CalculateAllMovements;
import com.kodilla.myEnum.KindOfMove;
import com.kodilla.myEnum.KindOfTempQueenMove;
import com.kodilla.myEnum.MovementOrKill;
import com.kodilla.myEnum.PawnType;
import com.kodilla.view.*;

import java.util.*;
import java.util.stream.Collectors;

public class RealMove {
    private CalculateAllMovements calculateAllMovements;
    private CalculateMaxKill calculateMaxKill;
    private CheckersApp view;
    private Field[][] board;
    private int moveNumber = 0;
    private List<Map<Position, Pawn>> positionsMap = new ArrayList<>();
    private boolean isEndOfRound = false;
//    private boolean isPlayerOne;

    //    public boolean isPlayerOne() {
//        return isPlayerOne;
//    }

    public RealMove(CalculateAllMovements calculateAllMovements, CalculateMaxKill calculateMaxKill, Field[][] board, CheckersApp view) {
        this.calculateAllMovements = calculateAllMovements;
        this.calculateMaxKill = calculateMaxKill;
        this.board = board;
        this.view = view;
//        this.isPlayerOne = true;
    }

    public boolean isEndOfRound() {
        return isEndOfRound;
    }

    public void checkingTheCorrectnessOfTheMovement(int x, int y, Pawn pawn) {
        Position positionOfTheNextMove = new Position(moveNumber, x, y);
        isEndOfRound = false;
        if ((x + y) % 2 != 0) {
            divisionOfPawnsDueToType(pawn, positionOfTheNextMove);
        } else {
            backPawnToLastPosition(pawn);
        }
    }

    private void divisionOfPawnsDueToType(Pawn pawn, Position positionOfTheNextMove) {
        if (pawn.getPawnType().equals(PawnType.BLACK)) {// && isPlayerOne) {
            testSwitch(pawn, positionOfTheNextMove, calculateMaxKill.getBlackPawnsWithMaxPossibility());
        } else if (pawn.getPawnType().equals(PawnType.WHITE)) { // && !isPlayerOne) {
            testSwitch(pawn, positionOfTheNextMove, calculateMaxKill.getWhitePawnsWithMaxPossibility());
        }
    }

    private void testSwitch(Pawn pawn, Position positionOfTheNextMove, Map<Pawn, List<Movement>> maxKillSet) {
        switch (checkIfThereIsSimpleMoveOrKill(pawn, positionOfTheNextMove, maxKillSet)) {
            case BACK:
                PhysicalMovement.backPawnToLastPosition(pawn);
                //backPawnToLastPosition(pawn);
                break;
            case SIMPLE_MOVE:
                simpleMove(pawn, positionOfTheNextMove);
                break;
            case FIRST_KILL:
                firstKillingFromSequence(pawn, positionOfTheNextMove, maxKillSet);
                break;
            case ANOTHER_KILL:
                doTheKill(pawn, positionOfTheNextMove);
                break;
        }
    }

    private MovementOrKill checkIfThereIsSimpleMoveOrKill(Pawn pawn, Position positionOfTheNextMove, Map<Pawn, List<Movement>> maxKillSet) {
        if (!maxKillSet.isEmpty()) {
            if (maxKillSet.containsKey(pawn)) {
                if (positionsMap.size() == 0) {
                    if (checkIfPossibleKill(maxKillSet.get(pawn), positionOfTheNextMove)) {
                        return MovementOrKill.FIRST_KILL;
                    } else {
                        return MovementOrKill.BACK;
                    }
                } else if (isNextPositionInPositionList(positionOfTheNextMove)) {
                    return MovementOrKill.ANOTHER_KILL;
                } else {
                    return MovementOrKill.BACK;
                }
            } else {
                return MovementOrKill.BACK;
            }
        } else if (checkIfPositionBelongsToSimpleMovements(pawn, positionOfTheNextMove)) {
            return MovementOrKill.SIMPLE_MOVE;
        } else {
            return MovementOrKill.BACK;
        }
    }

    private void simpleMove(Pawn pawn, Position positionOfTheNextMove) {
        if (!pawn.isQueen()) {
            doQueen(positionOfTheNextMove.getY(), pawn);
        }
        pawnMove(positionOfTheNextMove.getX(), positionOfTheNextMove.getY(), pawn);
//            isPlayerOne = !isPlayerOne;
        isEndOfRound = true;
    }

    private boolean checkIfPositionBelongsToSimpleMovements(Pawn pawn, Position positionOfTheNextMove) {
        if (pawn.isQueen()) {
            positionOfTheNextMove.setKind(KindOfTempQueenMove.SIMPLE_MOVE);
        }
        List<Movement> movements = calculateAllMovements.getPossibleMoves().get(pawn);
        return movements.stream()
                .filter(movement -> movement.getKindOfMove().equals(KindOfMove.MOVE_PAWN) ||
                        movement.getKindOfMove().equals(KindOfMove.MOVE_QUEEN))
                .anyMatch(movement -> movement.getNextPositions().containsKey(positionOfTheNextMove));
    }

    private void firstKillingFromSequence(Pawn pawn, Position positionOfTheNextMove, Map<Pawn, List<Movement>> maxKillSet) {
        List<Movement> allPawnMovements = maxKillSet.get(pawn);
        List<Movement> movementsContainsPosition = allPawnMovements.stream()
                .filter(movement -> movement.getNextPositions().containsKey(positionOfTheNextMove))
                .collect(Collectors.toList());
        for (int i = 0; i < movementsContainsPosition.size(); i++) {
            positionsMap.add(new LinkedHashMap<>());
            positionsMap.get(i).putAll(movementsContainsPosition.get(i).getNextPositions());
        }
        doTheKill(pawn, positionOfTheNextMove);
    }


    private boolean isNextPositionInPositionList(Position positionOfTheNextMove) {
        boolean is = positionsMap.stream()
                .flatMap(positionPawnMap -> positionPawnMap.entrySet().stream())
                .anyMatch(positionPawnEntry -> positionPawnEntry.getKey().equals(positionOfTheNextMove));
        if (is) {
            positionsMap = positionsMap.stream()
                    .filter(positionPawnMap -> positionPawnMap.containsKey(positionOfTheNextMove))
                    .collect(Collectors.toList());
        }
        return is;
    }

    private boolean checkIfPossibleKill(List<Movement> movements, Position positionOfTheNextMove) {
        return movements.stream()
                .flatMap(movement -> movement.getNextPositions().entrySet().stream())
                .anyMatch(positionPawnEntry -> positionPawnEntry.getKey().equals(positionOfTheNextMove));
    }


    private void doTheKill(Pawn pawn, Position positionOfTheNextMove) {
        List<Map<Position, Pawn>> positionsToRemove = calculationToThePositionsToRemoveFromGivenMove(positionsMap, moveNumber);
        removeCompactedPawns(positionsToRemove);
        pawnMove(positionOfTheNextMove.getX(), positionOfTheNextMove.getY(), pawn);
        removeTheMoveThatTookPlace(positionsMap, positionsToRemove);
        positionsToRemove.clear();
        moveNumber++;
        if (positionsMap.isEmpty()) {
            if (!pawn.isQueen()) {
                doQueen(positionOfTheNextMove.getY(), pawn);
            }
            //     isPlayerOne = !isPlayerOne;
            isEndOfRound = true;
            moveNumber = 0;
        }
    }

    private void removeTheMoveThatTookPlace(List<Map<Position, Pawn>> allValues, List<Map<Position, Pawn>> valuesToBeRemoved) {
        for (int i = 0; i < valuesToBeRemoved.size(); i++) {
            for (Map.Entry<Position, Pawn> entry : valuesToBeRemoved.get(i).entrySet()) {
                allValues.get(i).remove(entry.getKey());
            }
        }
        boolean isEmpty = allValues.stream()
                .allMatch(Map::isEmpty);
        if (isEmpty) {
            allValues.clear();
        }
    }

    private List<Map<Position, Pawn>> calculationToThePositionsToRemoveFromGivenMove(List<Map<Position, Pawn>> positionsMap, int moveNumber) {
        List<Map<Position, Pawn>> removeMap = new ArrayList<>();
        for (int i = 0; i < positionsMap.size(); i++) {
            removeMap.add(new LinkedHashMap<>());
            for (Map.Entry<Position, Pawn> entry : positionsMap.get(i).entrySet()) {
                if (entry.getKey().getMoveNumber() == moveNumber) {
                    removeMap.get(i).put(entry.getKey(), entry.getValue());
                }
            }
        }
        return removeMap;
    }


    private void removeCompactedPawns(List<Map<Position, Pawn>> removeMap) {
        for (Map<Position, Pawn> positionPawnMap : removeMap) {
            for (Map.Entry<Position, Pawn> entry : positionPawnMap.entrySet()) {
                if (entry.getValue() != null) {
                    calculateAllMovements.getPossibleMoves().remove(entry.getValue());
                    if (board[entry.getValue().getLastPositionX()][entry.getValue().getLastPositionY()].getPawn() != null) {
                        board[entry.getValue().getLastPositionX()][entry.getValue().getLastPositionY()].getPawn().setVisible(false);
                        board[entry.getValue().getLastPositionX()][entry.getValue().getLastPositionY()].setPawn(null);
                    }
                }
            }
        }
    }

    public void pawnMove(int nextX, int nextY, Pawn pawn) {
        board[pawn.getLastPositionX()][pawn.getLastPositionY()].setPawn(null);
        board[nextX][nextY].setPawn(pawn);
        pawn.move(nextX, nextY);
    }

    private void doQueen(int nextY, Pawn pawn) {
        if (pawn.getPawnType().equals(PawnType.BLACK) && nextY == 7) {
            pawn.setQueen(true);
            view.changePawnIntoAQueen(pawn);
        } else if (pawn.getPawnType().equals(PawnType.WHITE) && nextY == 0) {
            pawn.setQueen(true);
            view.changePawnIntoAQueen(pawn);
        }
    }

    private void backPawnToLastPosition(Pawn pawn) {
        pawn.move(pawn.getLastPositionX(), pawn.getLastPositionY());
    }
}
