package com.kodilla.playerMove;

import com.kodilla.movementCalculation.calculateAll.Calculate;
import com.kodilla.movementCalculation.dto.ElementsOfBoardDto;
import com.kodilla.movementCalculation.PawnAndPositions;
import com.kodilla.movementCalculation.PositionAndKilledPawn;
import com.kodilla.constantly.MovementOrKill;
import com.kodilla.constantly.PawnType;
import com.kodilla.oldElements.PhysicalMovement;
import com.kodilla.oldElements.Position;
import com.kodilla.view.CheckersApp;
import com.kodilla.oldElements.Field;
import com.kodilla.oldElements.Pawn;

import java.util.ArrayList;
import java.util.List;

public class MovingThePiece {

    private boolean isEndOfRound;
    private List<PawnAndPositions> temporaryMove = new ArrayList<>();
    private int moveNumber = 0;
    private PhysicalMovement physicalMovement;

    public MovingThePiece(CheckersApp view) {
        this.physicalMovement = new PhysicalMovement(view);
    }

    public boolean isEndOfRound() {
        return isEndOfRound;
    }

    public void checkingTheCorrectnessOfTheMovement(int x, int y, Pawn pawn, Field[][] board) {
        isEndOfRound = false;
        ElementsOfBoardDto elements = new ElementsOfBoardDto(pawn, board, x, y);
        Position positionOfTheNextMove = new Position(x, y);
        divisionOfPawnsDueToType(elements, positionOfTheNextMove);
    }

    private void divisionOfPawnsDueToType(ElementsOfBoardDto elements, Position positionOfTheNextMove) {
        if (elements.pawn.getPawnType().equals(PawnType.BLACK)) {// && isPlayerOne) {
            testSwitch(elements, positionOfTheNextMove, Calculate.getMaxMovesAmongBlack(elements.board));
        } else if (elements.pawn.getPawnType().equals(PawnType.WHITE)) { // && !isPlayerOne) {
            testSwitch(elements, positionOfTheNextMove, Calculate.getMaxMovesAmongWhite(elements.board));
        }
    }

    private void testSwitch(ElementsOfBoardDto elements, Position positionOfTheNextMove, List<PawnAndPositions> maxMovesAmongPawns) {
        switch (checkIfThereIsSimpleMoveOrKill(elements, positionOfTheNextMove, maxMovesAmongPawns)) {
            case BACK:
                PhysicalMovement.backPawnToLastPosition(elements.pawn);
                break;
            case SIMPLE_MOVE:
                simpleMove(elements, positionOfTheNextMove);
                break;
            case FIRST_KILL:
                firstKillingFromSequence(elements, positionOfTheNextMove, maxMovesAmongPawns);
                break;
            case ANOTHER_KILL:
                doTheKill(positionOfTheNextMove, elements);
                break;
        }
    }

    private void firstKillingFromSequence(ElementsOfBoardDto elements, Position positionOfTheNextMove, List<PawnAndPositions> maxMovesAmongPawns) {
        temporaryMove = takeThisPawnMovesIncludingNextPosition(positionOfTheNextMove, maxMovesAmongPawns, elements.pawn);
        doTheKill(positionOfTheNextMove, elements);
    }

    private void doTheKill(Position position, ElementsOfBoardDto elements) {
        List<Pawn> killedPawns = findKilledPawns(position);
        System.out.println("player killed pawn " + killedPawns);
        PhysicalMovement.removeCompactedPawns(killedPawns, elements.board);
        PhysicalMovement.pawnMove(position.getX(), position.getY(), elements.pawn, elements.board);
        moveNumber++;
        int lastMoveNumber = getLastMoveNumber();
        if (moveNumber > lastMoveNumber) {
            PhysicalMovement.doQueen(position.getY(), elements.pawn);
//                 isPlayerOne = !isPlayerOne;
            isEndOfRound = true;
            temporaryMove.clear();
            moveNumber = 0;
        }
    }

    private int getLastMoveNumber() {
        return temporaryMove.stream()
                .flatMap(pawnAndPositions -> pawnAndPositions.getPositions().stream())
                .mapToInt(PositionAndKilledPawn::getMoveNumber)
                .max().getAsInt();
    }

    private List<Pawn> findKilledPawns(Position positionOfTheNextMove) {
        List<Pawn> compactedPawns = new ArrayList<>();
        for (PawnAndPositions pawnAndPositions : temporaryMove) {
            for (PositionAndKilledPawn positionAndKilledPawn : pawnAndPositions.getPositions()) {
                if (positionAndKilledPawn.getPosition().equals(positionOfTheNextMove) &&
                        positionAndKilledPawn.getMoveNumber() == moveNumber) {
                    compactedPawns.add(positionAndKilledPawn.getCompactedPawn());
                }
            }
        }
        return compactedPawns;
    }

    private List<PawnAndPositions> takeThisPawnMovesIncludingNextPosition(Position position, List<PawnAndPositions> movesThisPawn, Pawn pawn) {
        List<PawnAndPositions> moveWithPosition = new ArrayList<>();
        for (PawnAndPositions pawnAndPositions : movesThisPawn) {
            if (pawnAndPositions.getPawnOwner().equals(pawn)) {
                for (PositionAndKilledPawn positionAndKilledPawn : pawnAndPositions.getPositions()) {
                    if (positionAndKilledPawn.getPosition().equals(position) &&
                            positionAndKilledPawn.getMoveNumber() == moveNumber) {
                        moveWithPosition.add(pawnAndPositions);
                    }
                }
            }
        }
        return moveWithPosition;
    }

    private void simpleMove(ElementsOfBoardDto elements, Position positionOfTheNextMove) {
        PhysicalMovement.doQueen(positionOfTheNextMove.getY(), elements.pawn);
        PhysicalMovement.pawnMove(positionOfTheNextMove.getX(), positionOfTheNextMove.getY(), elements.pawn, elements.board);
//            isPlayerOne = !isPlayerOne;
        isEndOfRound = true;
    }

    private MovementOrKill checkIfThereIsSimpleMoveOrKill(ElementsOfBoardDto elements, Position positionOfTheNextMove, List<PawnAndPositions> maxMovesAmongPawns) {
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

    private boolean containsPawnAndPosition(ElementsOfBoardDto elements, Position positionOfTheNextMove, List<PawnAndPositions> maxMovesAmongPawns) {
        return maxMovesAmongPawns.stream()
                .filter(pawnAndPositions -> pawnAndPositions.getPawnOwner().equals(elements.pawn))
                .flatMap(pawnAndPositions -> pawnAndPositions.getPositions().stream())
                .anyMatch(positionAndKilledPawn -> positionAndKilledPawn.getPosition().equals(positionOfTheNextMove) &&
                        positionAndKilledPawn.getMoveNumber() == moveNumber);

    }
}