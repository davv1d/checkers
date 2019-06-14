package com.kodilla.model.dataObject;

import com.kodilla.model.elementsOfTheBoard.Pawn;
import com.kodilla.model.elementsOfTheBoard.Position;

import java.util.ArrayList;
import java.util.List;

public class CheckedMovement {
    private boolean isEndOfRound;
    private boolean isCorrect;
    private boolean doQueen;
    private List<Position> positionKillPawns = new ArrayList<>();
    private Pawn pawn;
    private MoveData moveData;

    public CheckedMovement() {
        this.isCorrect = false;
        this.isEndOfRound = false;
        this.doQueen = false;
    }

    public CheckedMovement(boolean isEndOfRound, boolean isCorrect, List<Position> positionKillPawns, boolean doQueen) {
        this.isEndOfRound = isEndOfRound;
        this.isCorrect = isCorrect;
        this.positionKillPawns = positionKillPawns;
        this.doQueen = doQueen;
    }

    public CheckedMovement(boolean isEndOfRound, boolean isCorrect, List<Position> positionKillPawns, boolean doQueen,MoveData moveData ,Pawn pawn) {
        this.isEndOfRound = isEndOfRound;
        this.isCorrect = isCorrect;
        this.positionKillPawns = positionKillPawns;
        this.doQueen = doQueen;
        this.moveData = moveData;
        this.pawn = pawn;
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

    public void setMoveData(MoveData moveData) {
        this.moveData = moveData;
    }

    public boolean doQueen() {
        return doQueen;
    }

    public void setEndOfRound(boolean endOfRound) {
        isEndOfRound = endOfRound;
    }

    public boolean isEndOfRound() {
        return isEndOfRound;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public List<Position> getPositionsKilledPawns() {
        return positionKillPawns;
    }

    @Override
    public String toString() {
        return "CheckedMovement{" +
                "isCorrect=" + isCorrect +
                ", positionKillPawns=" + positionKillPawns +
                '}';
    }
}
