package com.kodilla.model.dataObject;

import com.kodilla.model.constantly.Winner;
import com.kodilla.model.elementsOfTheBoard.Pawn;
import com.kodilla.model.elementsOfTheBoard.Position;

import java.util.ArrayList;
import java.util.List;

public class ModelData {
    private boolean isEndOfRound;
    private boolean isTheRightMove;
    private boolean doQueen;
    private List<Position> positionKillPawns = new ArrayList<>();
    private Pawn pawn;
    private MoveData moveData;
    private Winner winner = Winner.NONE;

    public ModelData() {
        this.isTheRightMove = false;
        this.isEndOfRound = false;
        this.doQueen = false;
    }

    public ModelData(boolean isEndOfRound, boolean isTheRightMove, List<Position> positionKillPawns, boolean doQueen) {
        this.isEndOfRound = isEndOfRound;
        this.isTheRightMove = isTheRightMove;
        this.positionKillPawns = positionKillPawns;
        this.doQueen = doQueen;
    }

    public ModelData(boolean isEndOfRound, boolean isTheRightMove, List<Position> positionKillPawns, boolean doQueen, MoveData moveData, Pawn pawn) {
        this.isEndOfRound = isEndOfRound;
        this.isTheRightMove = isTheRightMove;
        this.positionKillPawns = positionKillPawns;
        this.doQueen = doQueen;
        this.moveData = moveData;
        this.pawn = pawn;
    }

    public boolean isWinner() {
        return !winner.equals(Winner.NONE);
    }

    public Winner getWinner() {
        return winner;
    }

    public void setWinner(Winner winner) {
        this.winner = winner;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public MoveData getMoveData() {
        return moveData;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    public boolean doQueen() {
        return doQueen;
    }

    public boolean isEndOfRound() {
        return isEndOfRound;
    }

    public boolean isTheRightMove() {
        return isTheRightMove;
    }

    public List<Position> getPositionsKilledPawns() {
        return positionKillPawns;
    }

}
