package com.kodilla.calculateTemporaryList;

import com.kodilla.myEnum.KindOfMove;
import com.kodilla.myEnum.PawnType;
import com.kodilla.testFindallMoves.Movement;
import com.kodilla.testFindallMoves.Position;
import com.kodilla.view.Field;
import com.kodilla.view.Pawn;

import java.util.*;

public class AllPawnPositions {
    private Field[][] board;
    private List<Position> temporaryMovesList = new ArrayList<>();

    private List<Pawn> temporaryKilledPawns = new ArrayList<>();
    private Position auxiliaryPosition = new Position(0, 0);
    private Map<Position, Pawn> testMap = new LinkedHashMap<>();
    private Map<Pawn, List<Movement>> possibleMoves;


    public AllPawnPositions(Field[][] board, Map<Pawn, List<Movement>> possibleMoves) {
        this.board = board;
        this.possibleMoves = possibleMoves;
    }

    public void clearList(){
        temporaryKilledPawns.clear();
        temporaryMovesList.clear();
        testMap.clear();
    }

    public List<Position> getTemporaryMovesList() {
        return temporaryMovesList;
    }

    public Map<Position, Pawn> getTestMap() {
        return testMap;
    }

    public void movementOfPawnsDependingOnType(Pawn pawn) {
        if (pawn.getPawnType().equals(PawnType.BLACK)) {
            searchMovesForBlackPawns(pawn);
        } else {
            searchMovesForWhitePawns(pawn);
        }
    }

    private void searchMovesForWhitePawns(Pawn pawn) {
        int x = pawn.getLastPositionX();
        int y = pawn.getLastPositionY();
        searchWhiteMoves(x, y, pawn);
        searchPawnKill(x, y, pawn);
    }

    private void searchMovesForBlackPawns(Pawn pawn) {
        int x = pawn.getLastPositionX();
        int y = pawn.getLastPositionY();
        searchBlackMoves(x, y, pawn);
        searchPawnKill(x, y, pawn);
    }

    private void searchWhiteMoves(int x, int y, Pawn pawn) {
        if (x > 0 && x < 7 && y > 0) {
            searchEmptyField(x - 1, y - 1, pawn);
            searchEmptyField(x + 1, y - 1, pawn);
        } else if (x == 0 && y > 0) {
            searchEmptyField(x + 1, y - 1, pawn);
        } else if (x == 7 && y > 0) {
            searchEmptyField(x - 1, y - 1, pawn);
        } else {
            //don't move
        }
    }

    private void searchBlackMoves(int x, int y, Pawn pawn) {
        if (x > 0 && x < 7 && y < 7) {
            searchEmptyField(x - 1, y + 1, pawn);
            searchEmptyField(x + 1, y + 1, pawn);
        } else if (x == 0 && y < 7) {
            searchEmptyField(x + 1, y + 1, pawn);
        } else if (x == 7 && y < 7) {
            searchEmptyField(x - 1, y + 1, pawn);
        } else {
            //don't move
        }
    }

    private void searchEmptyField(int x, int y, Pawn pawn) {
        if (!board[x][y].hasPawn()) {
            Movement type = new Movement(KindOfMove.MOVE_PAWN);
            type.addNextPositions(new Position(x, y), null);
            possibleMoves.get(pawn).add(type);
        }
    }















    private void searchPawnKill(int x, int y, Pawn pawn) {
        if (y <= 1) {
            firstRectangleYSmallerOrEqual1(x, y, pawn);
            temporaryMovesList.add(auxiliaryPosition);
            backMoveRemovePawnFromTemporaryKilledList();
        } else if (y >= 2 && y <= 5) {
            secondRectangleYBetween2And5(x, y, pawn);
            temporaryMovesList.add(auxiliaryPosition);
            backMoveRemovePawnFromTemporaryKilledList();
        } else {
            thirdRectangleYBiggerOrEqual6(x, y, pawn);
            temporaryMovesList.add(auxiliaryPosition);
            backMoveRemovePawnFromTemporaryKilledList();
        }
    }

    private void backMoveRemovePawnFromTemporaryKilledList() {
        if (temporaryKilledPawns.size() > 0)
            temporaryKilledPawns.remove(temporaryKilledPawns.size() - 1);
    }


    private void firstRectangleYSmallerOrEqual1(int x, int y, Pawn pawn) {
        if (x <= 1) {
            canPawnKill(x + 2, y + 2, x + 1, y + 1, pawn);
        } else if (x >= 2 && x <= 5) {
            canPawnKill(x - 2, y + 2, x - 1, y + 1, pawn);
            canPawnKill(x + 2, y + 2, x + 1, y + 1, pawn);
        } else {
            canPawnKill(x - 2, y + 2, x - 1, y + 1, pawn);
        }
    }

    private void secondRectangleYBetween2And5(int x, int y, Pawn pawn) {
        if (x <= 1) {
            canPawnKill(x + 2, y - 2, x + 1, y - 1, pawn);
            canPawnKill(x + 2, y + 2, x + 1, y + 1, pawn);
        } else if (x >= 2 && x <= 5) {
            canPawnKill(x + 2, y - 2, x + 1, y - 1, pawn);
            canPawnKill(x + 2, y + 2, x + 1, y + 1, pawn);
            canPawnKill(x - 2, y + 2, x - 1, y + 1, pawn);
            canPawnKill(x - 2, y - 2, x - 1, y - 1, pawn);
        } else {
            canPawnKill(x - 2, y - 2, x - 1, y - 1, pawn);
            canPawnKill(x - 2, y + 2, x - 1, y + 1, pawn);
        }
    }

    private void thirdRectangleYBiggerOrEqual6(int x, int y, Pawn pawn) {
        if (x <= 1) {
            canPawnKill(x + 2, y - 2, x + 1, y - 1, pawn);
        } else if (x >= 2 && x <= 5) {
            canPawnKill(x - 2, y - 2, x - 1, y - 1, pawn);
            canPawnKill(x + 2, y - 2, x + 1, y - 1, pawn);
        } else {
            canPawnKill(x - 2, y - 2, x - 1, y - 1, pawn);
        }
    }

    private void canPawnKill(int newX, int newY, int opponentX, int opponentY, Pawn pawn) {
        Field nextStepField = board[newX][newY];
        Field fieldToJump = board[opponentX][opponentY];
        if (!nextStepField.hasPawn() || nextStepField.getPawn().equals(pawn))
            if (fieldToJump.hasPawn() && !temporaryKilledPawns.contains(fieldToJump.getPawn()))
                if (!fieldToJump.getPawn().getPawnType().equals(pawn.getPawnType())) {
                    temporaryKilledPawns.add(fieldToJump.getPawn());
                    Position position = new Position(newX, newY);
                    temporaryMovesList.add(position);
                    testMap.put(position, fieldToJump.getPawn());
                    searchPawnKill(newX, newY, pawn);
                }
    }
}
