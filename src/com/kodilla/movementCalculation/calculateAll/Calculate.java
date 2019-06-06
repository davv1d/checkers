package com.kodilla.movementCalculation.calculateAll;

import com.kodilla.movementCalculation.PawnAndPositions;
import com.kodilla.movementCalculation.pawn.SearchKillForPawns;
import com.kodilla.movementCalculation.queen.SearchQueenMoves;
import com.kodilla.constantly.PawnType;

import com.kodilla.oldElements.Field;
import com.kodilla.oldElements.Pawn;
import java.util.*;

import static com.kodilla.movementCalculation.pawn.SearchMovesForPawns.*;

public class Calculate {

    private static List<PawnAndPositions> calculateAllMoves(Field[][] board) {
        List<PawnAndPositions> allPawnsAndPositions = new ArrayList<>();
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
    private static void movementOfPawnsDependingOnType(Pawn pawn, Field[][] board, List<PawnAndPositions> allPawnsAndPositions) {
        if (pawn.getPawnType().equals(PawnType.BLACK)) {
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

    public static List<PawnAndPositions> getMaxMovesAmongBlack(Field[][] board) {
        List<PawnAndPositions> pawnsAndPositions = calculateAllMoves(board);
        return CalculateMaxKill.findMaxKillAmongAll(pawnsAndPositions, PawnType.BLACK);
    }

    public static List<PawnAndPositions> getMaxMovesAmongWhite(Field[][] board) {
        List<PawnAndPositions> pawnsAndPositions = calculateAllMoves(board);
        return CalculateMaxKill.findMaxKillAmongAll(pawnsAndPositions, PawnType.WHITE);
    }
}
