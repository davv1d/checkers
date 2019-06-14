package com.kodilla.model.userMoves;

import com.kodilla.model.boardBeaviour.ActionOnTheBoard;
import com.kodilla.model.elementsOfTheBoard.Pawn;
import com.kodilla.model.dataObject.MoveData;
import com.kodilla.model.dataObject.CheckedMovement;
import com.kodilla.model.stateOfTheGame.Game;

public abstract class PlayerMove {
    protected Game game;

    public PlayerMove(Game game) {
        this.game = game;
    }

    protected void doAllBehaviour(MoveData moveData, Pawn pawn, CheckedMovement checkedMovement) {
        if (checkedMovement.isCorrect()) {
            ActionOnTheBoard.removingKilledPawns(checkedMovement.getPositionsKilledPawns(), game.getGameBoard());
            ActionOnTheBoard.realMove(moveData, pawn, game.getGameBoard());
            ActionOnTheBoard.doQueen(pawn, checkedMovement.doQueen());
        }
    }
}
