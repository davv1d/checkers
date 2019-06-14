package com.kodilla.model.movementCalculation.pawn;

import com.kodilla.model.movementCalculation.Coordinates;
import com.kodilla.model.movementCalculation.MoveOfPawn;
import com.kodilla.model.movementCalculation.OneStepMove;
import com.kodilla.model.dataObject.ItemsOfBoard;
import com.kodilla.model.constantly.KindOfPosition;
import com.kodilla.model.elementsOfTheBoard.Position;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;

import java.util.List;
import java.util.function.IntFunction;
import java.util.function.Predicate;

import static com.kodilla.model.constantly.DefaultCoordinates.*;


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
        ItemsOfBoard items = new ItemsOfBoard(pawn, board, pawn.getLastPositionX(), pawn.getLastPositionY());
        Coordinates coordinates = new Coordinates(null, calculateY);
        searchMoves(allPawnsAndPositions, items, coordinates, condition);
    }

    private static void searchMoves(List<MoveOfPawn> allPawnsAndPositions, ItemsOfBoard items, Coordinates coordinates, Predicate<Integer> condition) {
        if (items.getX() > 0 && items.getX() < 7 && condition.test(items.getY())) {
            coordinates.setCalculateX(subtractFromPosition);
            searchEmptyField(allPawnsAndPositions, coordinates, items);
            coordinates.setCalculateX(addToPosition);
            searchEmptyField(allPawnsAndPositions, coordinates, items);
        } else if (items.getX() == 0 && condition.test(items.getY())) {
            coordinates.setCalculateX(addToPosition);
            searchEmptyField(allPawnsAndPositions, coordinates, items);
        } else if (items.getX() == 7 && condition.test(items.getY())) {
            coordinates.setCalculateX(subtractFromPosition);
            searchEmptyField(allPawnsAndPositions, coordinates, items);
        }
    }

    private static void searchEmptyField(List<MoveOfPawn> allPawnsAndPositions, Coordinates coordinates, ItemsOfBoard items) {
        coordinates.setX_Y(items.getX(), items.getY());
        coordinates.calculate();
        if (!items.getBoard()[coordinates.getX()][coordinates.getY()].hasPawn()) {
            Position position = new Position(coordinates.getX(), coordinates.getY());
            OneStepMove oneStepMove = new OneStepMove(position, null, KindOfPosition.SIMPLE_MOVE);
            MoveOfPawn moveOfPawn = new MoveOfPawn(items.getPawn());
            moveOfPawn.addPosition(oneStepMove);
            allPawnsAndPositions.add(moveOfPawn);
        }
    }

}
