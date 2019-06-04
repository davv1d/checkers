package com.kodilla.checkersExperience;

import com.kodilla.myEnum.PawnType;

import com.kodilla.view.Field;
import com.kodilla.view.Pawn;
import java.util.*;

import static com.kodilla.checkersExperience.SearchMovesForPawns.*;

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

    public static List<PawnAndPositions> getAllMaxMoves(Field[][] board) {
        List<PawnAndPositions> allMoves = new ArrayList<>();
        List<PawnAndPositions> pawnsAndPositions = calculateAllMoves(board);
        allMoves.addAll(CalculateMaxKill.findMaxKillAmongAll(pawnsAndPositions, PawnType.WHITE));
        allMoves.addAll(CalculateMaxKill.findMaxKillAmongAll(pawnsAndPositions, PawnType.BLACK));
        return allMoves;
    }

}
