package com.kodilla.model.playerMoveCorrectness;

import com.kodilla.model.boardBeaviour.Conditions;
import com.kodilla.model.dataObject.CheckedMovement;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;
import com.kodilla.model.elementsOfTheBoard.Position;
import com.kodilla.model.movementCalculation.calculateAll.Calculate;
import com.kodilla.model.dataObject.ItemsOfBoard;
import com.kodilla.model.movementCalculation.MoveOfPawn;
import com.kodilla.model.movementCalculation.OneStepMove;
import com.kodilla.model.constantly.MovementOrKill;

import java.util.ArrayList;
import java.util.List;

public class MovingThePiece {
    private boolean isEndOfRound;
    private List<MoveOfPawn> temporaryMove = new ArrayList<>();
    private int moveNumber = 0;

    public CheckedMovement checkingTheCorrectnessOfTheMovement(int x, int y, Pawn pawn, Field[][] board) {
        isEndOfRound = false;
        ItemsOfBoard items = new ItemsOfBoard(pawn, board, x, y);
        Position positionOfTheNextMove = new Position(x, y);
        return divisionOfPawnsDueToType(items, positionOfTheNextMove);
    }

    private CheckedMovement divisionOfPawnsDueToType(ItemsOfBoard items, Position positionOfTheNextMove) {
        if (items.getPawn().isBlack()) {// && isPlayerOne) {
            return testSwitch(items, positionOfTheNextMove, Calculate.getMaxMovesAmongBlack(items.getBoard()));
        } else { // && !isPlayerOne) {
            return testSwitch(items, positionOfTheNextMove, Calculate.getMaxMovesAmongWhite(items.getBoard()));
        }
    }

    private CheckedMovement testSwitch(ItemsOfBoard items, Position positionOfTheNextMove, List<MoveOfPawn> maxMovesAmongPawns) {
        switch (checkIfThereIsSimpleMoveOrKill(items, positionOfTheNextMove, maxMovesAmongPawns)) {
            case BACK:
                return new CheckedMovement();
            case FIRST_KILL:
                return firstKillingFromSequence(items, positionOfTheNextMove, maxMovesAmongPawns);
            case ANOTHER_KILL:
                return doTheKill(positionOfTheNextMove, items);
        }
        return null;
    }

    private CheckedMovement firstKillingFromSequence(ItemsOfBoard items, Position positionOfTheNextMove, List<MoveOfPawn> maxMovesAmongPawns) {
        temporaryMove = takeThisPawnMovesIncludingNextPosition(positionOfTheNextMove, maxMovesAmongPawns, items.getPawn());
        return doTheKill(positionOfTheNextMove, items);
    }

    private CheckedMovement doTheKill(Position position, ItemsOfBoard items) {
        List<Pawn> killedPawns = findKilledPawns(position);
        List<Position> positionsKilledPawns = new ArrayList<>();
        if (!killedPawns.isEmpty()) {
            killedPawns.forEach(pawn -> positionsKilledPawns.add(new Position(pawn.getLastPositionX(), pawn.getLastPositionY())));
        }
        moveNumber++;
        int lastMoveNumber = getLastMoveNumber();
        if (moveNumber > lastMoveNumber) {
            isEndOfRound = true;
            temporaryMove.clear();
            moveNumber = 0;
        }
        boolean doQueen = Conditions.doQueen(items.getPawn(), position.getY(), isEndOfRound);
        return new CheckedMovement(isEndOfRound, true, positionsKilledPawns, doQueen);
    }

    private int getLastMoveNumber() {
        return temporaryMove.stream()
                .flatMap(pawnAndPositions -> pawnAndPositions.getPositions().stream())
                .mapToInt(OneStepMove::getMoveNumber)
                .max().getAsInt();
    }

    private List<Pawn> findKilledPawns(Position positionOfTheNextMove) {
        List<Pawn> compactedPawns = new ArrayList<>();
        for (MoveOfPawn moveOfPawn : temporaryMove) {
            for (OneStepMove oneStepMove : moveOfPawn.getPositions()) {
                if (oneStepMove.getPosition().equals(positionOfTheNextMove) &&
                        oneStepMove.getMoveNumber() == moveNumber &&
                        oneStepMove.hasCompactedPawn()) {
                    compactedPawns.add(oneStepMove.getCompactedPawn());
                }
            }
        }
        return compactedPawns;
    }

    private List<MoveOfPawn> takeThisPawnMovesIncludingNextPosition(Position position, List<MoveOfPawn> movesThisPawn, Pawn pawn) {
        List<MoveOfPawn> moveWithPosition = new ArrayList<>();
        for (MoveOfPawn moveOfPawn : movesThisPawn) {
            if (moveOfPawn.getPawnOwner().equals(pawn)) {
                for (OneStepMove oneStepMove : moveOfPawn.getPositions()) {
                    if (oneStepMove.getPosition().equals(position) &&
                            oneStepMove.getMoveNumber() == moveNumber) {
                        moveWithPosition.add(moveOfPawn);
                    }
                }
            }
        }
        return moveWithPosition;
    }

    private MovementOrKill checkIfThereIsSimpleMoveOrKill(ItemsOfBoard elements, Position positionOfTheNextMove, List<MoveOfPawn> maxMovesAmongPawns) {
        if (!maxMovesAmongPawns.isEmpty()) {
            if (temporaryMove.isEmpty() && containsPawnAndPosition(elements, positionOfTheNextMove, maxMovesAmongPawns)) {
                return MovementOrKill.FIRST_KILL;
            } else if (!temporaryMove.isEmpty() && containsPawnAndPosition(elements, positionOfTheNextMove, temporaryMove)) {
                return MovementOrKill.ANOTHER_KILL;
            } else {
                return MovementOrKill.BACK;
            }
        } else {
            System.out.println("no move");
            return MovementOrKill.BACK;
        }
    }

    private boolean containsPawnAndPosition(ItemsOfBoard items, Position positionOfTheNextMove, List<MoveOfPawn> maxMovesAmongPawns) {
        return maxMovesAmongPawns.stream()
                .filter(pawnAndPositions -> pawnAndPositions.getPawnOwner().equals(items.getPawn()))
                .flatMap(pawnAndPositions -> pawnAndPositions.getPositions().stream())
                .anyMatch(positionAndKilledPawn -> positionAndKilledPawn.getPosition().equals(positionOfTheNextMove) &&
                        positionAndKilledPawn.getMoveNumber() == moveNumber);

    }
}
