package com.kodilla.view;

import com.kodilla.interafeTest.CalculateAllMovements;
import com.kodilla.testFindallMoves.CalculateMaxKill;
import com.kodilla.testFindallMoves.RealMove;

public class Game {

    private CalculateAllMovements calculateAllMovements;
    private RealMove realMove;


    public Game(Field[][] board, CheckersApp view) {
        this.calculateAllMovements = new CalculateAllMovements(board);
        CalculateMaxKill calculateMaxKill = new CalculateMaxKill(calculateAllMovements);
        this.realMove = new RealMove(calculateAllMovements, calculateMaxKill, board, view);
        calculateAllMovements.search();
    }

    public void tryMove(int x, int y, Pawn pawn) {
        realMove.checkingTheCorrectnessOfTheMovement(x, y, pawn);
        System.out.println(realMove.isEndOfRound());
        calculateAllMovements.search();
    }

    private void displayMap() {
        calculateAllMovements.getPossibleMoves().entrySet().stream()
                .sorted((o1, o2) -> {
                    if (o1.getKey().getLastPositionY() < o2.getKey().getLastPositionY()) {
                        return -1;
                    } else if (o1.getKey().getLastPositionY() == o2.getKey().getLastPositionY()) {
                        if (o1.getKey().getLastPositionX() < o2.getKey().getLastPositionX()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    } else {
                        return 1;
                    }
                })
                .map(pawnListEntry -> pawnListEntry.getKey() + " | " + pawnListEntry.getValue().size())
                .forEach(System.out::println);
    }
}
