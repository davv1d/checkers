package com.kodilla.calculateTemporaryList;

import com.kodilla.myEnum.KindOfTempQueenMove;
import com.kodilla.testFindallMoves.Position;
import com.kodilla.view.Field;
import com.kodilla.view.Pawn;

import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;

public class AllQueenPositions {

    private List<Position> temporaryMovesList = new ArrayList<>();

    private List<Pawn> temporaryKilledPawns = new ArrayList<>();

    private Position positionQueenEnd = new Position(8, 8, KindOfTempQueenMove.BACK);

    private Field[][] board;
    private IntFunction<Integer> addToPosition = p -> p + 1;
    private IntFunction<Integer> subtractFromPosition = p -> p - 1;

    public AllQueenPositions(Field[][] board) {
        this.board = board;
    }

    public List<Position> getTemporaryMovesList(Pawn pawn) {
        movementOfQueen(pawn);
        List<Position> temporaryMovesList = new ArrayList<>(this.temporaryMovesList);
        this.temporaryMovesList.clear();
        return temporaryMovesList;
    }

    private void movementOfQueen(Pawn pawn) {
        int lastPositionX = pawn.getLastPositionX();
        int lastPositionY = pawn.getLastPositionY();
        board[lastPositionX][lastPositionY].setPawn(null);
        testIf(lastPositionX, lastPositionY, pawn);
        board[lastPositionX][lastPositionY].setPawn(pawn);
    }

    private void test(boolean isDiagonal, boolean isKill, Pawn pawn, int x, int y, IntFunction<Integer> calculateNextX, IntFunction<Integer> calculateNextY) {
        int sizeBefore = temporaryKilledPawns.size();
        x = calculateNextX.apply(x);
        y = calculateNextY.apply(y);
        if (!(x < 0 || x > 7 || y < 0 || y > 7)) {
            if (!board[x][y].hasPawn() && !isKill && !isDiagonal) {
                temporaryMovesList.add(new Position(x, y, KindOfTempQueenMove.SIMPLE_MOVE));
                test(false, false, pawn, x, y, calculateNextX, calculateNextY);
            } else if (!board[x][y].hasPawn() && isKill && !isDiagonal) {
                temporaryMovesList.add(new Position(x, y, KindOfTempQueenMove.NONE));
                testDiagonalKill(true, false, pawn, x, y, calculateNextX, calculateNextY);
                test(false, true, pawn, x, y, calculateNextX, calculateNextY);
            } else if (!board[x][y].hasPawn() && !isKill && isDiagonal) {
                test(true, false, pawn, x, y, calculateNextX, calculateNextY);
            } else if (board[x][y].hasPawn() && !board[x][y].getPawn().getPawnType().equals(pawn.getPawnType())) {
                checkQueenKill(isDiagonal, pawn, x, y, calculateNextX, calculateNextY);
            }
        }
        int sizeAfter = temporaryKilledPawns.size();
        removePawnWithTemporaryList(sizeBefore, sizeAfter);
    }

    private void removePawnWithTemporaryList(int sizeBefore, int sizeAfter) {
        if (sizeBefore != sizeAfter) {
            for (int i = sizeAfter - 1; i >= sizeBefore; i--) {
                temporaryKilledPawns.remove(i);
            }
        }
    }

    private void checkQueenKill(boolean isDiagonal, Pawn pawn, int x, int y, IntFunction<Integer> calculateNextX, IntFunction<Integer> calculateNextY) {
        if (x > 0 && x < 7 && y > 0 && y < 7) {
            int nextX = calculateNextX.apply(x);
            int nextY = calculateNextY.apply(y);
            if (!board[nextX][nextY].hasPawn() && !temporaryKilledPawns.contains(board[x][y].getPawn())) {
                if (isDiagonal) {
                    temporaryMovesList.add(new Position(x, y, KindOfTempQueenMove.KILL_DIAGONALLY));
                } else {
                    temporaryMovesList.add(new Position(x, y, KindOfTempQueenMove.KILL_IN_LINE));
                }
                temporaryKilledPawns.add(board[x][y].getPawn());
                test(false, true, pawn, x, y, calculateNextX, calculateNextY);
                temporaryMovesList.add(positionQueenEnd);
            }
        }
    }

    private void testDiagonalKill(boolean isDiagonal, boolean isKill, Pawn pawn, int x, int y, IntFunction<Integer> calculateNextX, IntFunction<Integer> calculateNextY) {
        if ((calculateNextX.equals(addToPosition) && calculateNextY.equals(subtractFromPosition)) ||
                (calculateNextX.equals(subtractFromPosition) && calculateNextY.equals(addToPosition))) {
            test(isDiagonal, isKill, pawn, x, y, addToPosition, addToPosition);
            test(isDiagonal, isKill, pawn, x, y, subtractFromPosition, subtractFromPosition);
        } else if ((calculateNextX.equals(addToPosition) && calculateNextY.equals(addToPosition)) ||
                (calculateNextX.equals(subtractFromPosition) && calculateNextY.equals(subtractFromPosition))) {
            test(isDiagonal, isKill, pawn, x, y, subtractFromPosition, addToPosition);
            test(isDiagonal, isKill, pawn, x, y, addToPosition, subtractFromPosition);
        }
    }

    private void testIf(int x, int y, Pawn pawn) {
        if (x == 0 && y == 7) {
            test(false, false, pawn, x, y, addToPosition, subtractFromPosition);
        } else if (x == 0 && y < 7) {
            test(false, false, pawn, x, y, addToPosition, addToPosition);
            test(false, false, pawn, x, y, addToPosition, subtractFromPosition);
        } else if (x < 7 && y == 0) {
            test(false, false, pawn, x, y, addToPosition, addToPosition);
            test(false, false, pawn, x, y, subtractFromPosition, addToPosition);
        } else if (x == 7 && y == 0) {
            test(false, false, pawn, x, y, subtractFromPosition, addToPosition);
        } else if (x == 7 && y > 0) {
            test(false, false, pawn, x, y, subtractFromPosition, addToPosition);
            test(false, false, pawn, x, y, subtractFromPosition, subtractFromPosition);
        } else if (x > 0 && y == 7) {
            test(false, false, pawn, x, y, addToPosition, subtractFromPosition);
            test(false, false, pawn, x, y, subtractFromPosition, subtractFromPosition);
        } else if (x > 0 && x < 7 && y > 0 && y < 7) {
            test(false, false, pawn, x, y, addToPosition, subtractFromPosition);
            test(false, false, pawn, x, y, addToPosition, addToPosition);
            test(false, false, pawn, x, y, subtractFromPosition, addToPosition);
            test(false, false, pawn, x, y, subtractFromPosition, subtractFromPosition);
        }
    }
}
