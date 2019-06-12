package com.kodilla.movementCalculation.pawn;

import com.kodilla.movementCalculation.Coordinates;
import com.kodilla.movementCalculation.MoveOfPawn;
import com.kodilla.movementCalculation.OneStepMove;
import com.kodilla.movementCalculation.dto.ElementsOfBoardDto;
import com.kodilla.constantly.KindOfPosition;
import com.kodilla.oldElements.Position;
import com.kodilla.oldElements.Field;
import com.kodilla.oldElements.Pawn;

import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import static com.kodilla.constantly.DefaultCoordinates.*;


public class SearchMovesForPawns {

    private static Predicate<Integer> lessThan7 = integer -> integer < 7;
    private static Predicate<Integer> moreThan0 = integer -> integer > 0;

    public static void searchMovesForWhitePawns(Pawn pawn, Field[][] board, List<MoveOfPawn> allPawnsAndPositions) {
        searchMovesForPawn(pawn, board, allPawnsAndPositions, subtractFromPosition, moreThan0);
    }


    public static void searchMovesForBlackPawns(Pawn pawn, Field[][] board, List<MoveOfPawn> allPawnsAndPositions) {
        searchMovesForPawn(pawn, board, allPawnsAndPositions, addToPosition, lessThan7);
    }

    private static void searchMovesForPawn(Pawn pawn, Field[][] board, List<MoveOfPawn> allPawnsAndPositions, IntFunction<Integer> calculateY, Predicate<Integer> condition) {
        ElementsOfBoardDto elements = new ElementsOfBoardDto(pawn, board, pawn.getLastPositionX(), pawn.getLastPositionY());
        Coordinates coordinates = new Coordinates(null, calculateY);
        searchMoves(allPawnsAndPositions, elements, coordinates, condition);
    }

    private static void searchMoves(List<MoveOfPawn> allPawnsAndPositions, ElementsOfBoardDto elements, Coordinates coordinates, Predicate<Integer> condition) {
        if (elements.x > 0 && elements.x < 7 && condition.test(elements.y)) {
            coordinates.setCalculateX(subtractFromPosition);
            searchEmptyField(allPawnsAndPositions, coordinates, elements);
            coordinates.setCalculateX(addToPosition);
            searchEmptyField(allPawnsAndPositions, coordinates, elements);
        } else if (elements.x == 0 && condition.test(elements.y)) {
            coordinates.setCalculateX(addToPosition);
            searchEmptyField(allPawnsAndPositions, coordinates, elements);
        } else if (elements.x == 7 && condition.test(elements.y)) {
            coordinates.setCalculateX(subtractFromPosition);
            searchEmptyField(allPawnsAndPositions, coordinates, elements);
        }
    }

    private static void searchEmptyField(List<MoveOfPawn> allPawnsAndPositions, Coordinates coordinates, ElementsOfBoardDto elements) {
        coordinates.setX_Y(elements.x, elements.y);
        coordinates.calculate();
        if (!elements.board[coordinates.getX()][coordinates.getY()].hasPawn()) {
            Position position = new Position(coordinates.getX(), coordinates.getY());
            OneStepMove oneStepMove = new OneStepMove(position, null, KindOfPosition.SIMPLE_MOVE);
            MoveOfPawn moveOfPawn = new MoveOfPawn(elements.pawn);
            moveOfPawn.addPosition(oneStepMove);
            allPawnsAndPositions.add(moveOfPawn);
        }
    }

}
