package com.kodilla.model.movementCalculation.calculateAll;

import com.kodilla.model.movementCalculation.MoveOfPawn;
import com.kodilla.model.movementCalculation.pawn.SearchKillForPawns;
import com.kodilla.model.movementCalculation.queen.SearchQueenMoves;

import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;
import java.util.*;

import static com.kodilla.model.movementCalculation.pawn.SearchMovesForPawns.*;

public class Calculate {

    private static List<MoveOfPawn> calculateAllMoves(Field[][] board) {
        List<MoveOfPawn> allPawnsAndPositions = new ArrayList<>();
        for (Field[] fields : board) {
            for (Field field : fields) {
                if (field.hasPawn() && !field.getPawn().isQueen()) {
                    movementOfPawnsDependingOnType(field.getPawn(), board, allPawnsAndPositions);
                } else if (field.hasPawn() && field.getPawn().isQueen()) {
                    SearchQueenMoves.movementOfQueen(field.getPawn(), board, allPawnsAndPositions);
                }
            }
        }
        return allPawnsAndPositions;
    }
    private static void movementOfPawnsDependingOnType(Pawn pawn, Field[][] board, List<MoveOfPawn> allPawnsAndPositions) {
        if (pawn.isBlack()) {
            boolean isKill = SearchKillForPawns.searchKillForPawn(pawn, board, allPawnsAndPositions);
            if (!isKill) {
                searchMovesForBlackPawns(pawn, board, allPawnsAndPositions);
            }
        } else {
            boolean isKill = SearchKillForPawns.searchKillForPawn(pawn, board, allPawnsAndPositions);
            if (!isKill) {
                searchMovesForWhitePawns(pawn, board, allPawnsAndPositions);
            }
        }
    }

    public static List<MoveOfPawn> getMaxMovesAmongBlack(Field[][] board) {
        List<MoveOfPawn> pawnsAndPositions = calculateAllMoves(board);
        return CalculateMaxKill.findMaxKillAmongAll(pawnsAndPositions, true);
    }

    public static List<MoveOfPawn> getMaxMovesAmongWhite(Field[][] board) {
        List<MoveOfPawn> pawnsAndPositions = calculateAllMoves(board);
        return CalculateMaxKill.findMaxKillAmongAll(pawnsAndPositions, false);
    }
}
