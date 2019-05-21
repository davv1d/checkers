package com.kodilla.interafeTest;

import com.kodilla.calculateTemporaryList.AllPawnPositions;
import com.kodilla.calculateTemporaryList.AllQueenPositions;
import com.kodilla.testFindallMoves.Movement;
import com.kodilla.testFindallMoves.Position;
import com.kodilla.view.*;

import java.util.*;


public class CalculateAllMovements {
    private Map<Pawn, List<Movement>> possibleMoves = new HashMap<>();
    private Field[][] board;
    private CalculateQueenMovement calculateQueenMovement;
    private CalculatePawnMovement calculatePawnMovement;
    private AllQueenPositions allQueenPositions;
    private AllPawnPositions allPawnPositions;

    public CalculateAllMovements(Field[][] board) {
        this.board = board;
        calculateQueenMovement = new CalculateQueenMovement(board);
        calculatePawnMovement = new CalculatePawnMovement();
        allQueenPositions = new AllQueenPositions(board);
        initializeMap();
        allPawnPositions = new AllPawnPositions(board, this.possibleMoves);
    }

    public Map<Pawn, List<Movement>> getPossibleMoves() {
        return possibleMoves;
    }

    private void initializeMap() {
        for (int y = 0; y < CheckersApp.HEIGHT; y++) {
            for (int x = 0; x < CheckersApp.WIDTH; x++) {
                if (board[x][y].hasPawn()) {
                    possibleMoves.put(board[x][y].getPawn(), new ArrayList<>());
                }
            }
        }
    }

    private void clearTheMapFromMovements() {
        for (int y = 0; y < CheckersApp.HEIGHT; y++) {
            for (int x = 0; x < CheckersApp.WIDTH; x++) {
                if (board[x][y].hasPawn()) {
                    possibleMoves.get(board[x][y].getPawn()).clear();
                }
            }
        }
    }

    public void search() {
        clearTheMapFromMovements();
        for (Map.Entry<Pawn, List<Movement>> entry : getPossibleMoves().entrySet()) {
            if (!entry.getKey().isQueen()) {
                allPawnPositions.movementOfPawnsDependingOnType(entry.getKey());
                List<Position> temporaryMovesList = allPawnPositions.getTemporaryMovesList();
                Map<Position, Pawn> testMap = allPawnPositions.getTestMap();
                List<Movement> allPossiblePawnPositions = calculatePawnMovement.addMovesToMap(temporaryMovesList, testMap);
                possibleMoves.get(entry.getKey()).addAll(allPossiblePawnPositions);
                allPawnPositions.clearList();
            } else {
                List<Position> temporaryMovesList = allQueenPositions.getTemporaryMovesList(entry.getKey());
                List<Movement> allPossibleQueenPositions = calculateQueenMovement.getAllPossiblePositions(temporaryMovesList, entry.getKey());
                possibleMoves.get(entry.getKey()).addAll(allPossibleQueenPositions);
            }
        }
    }
}
