package com.kodilla.model.userMoves;

import com.kodilla.model.elementsOfTheBoard.Pawn;
import com.kodilla.model.dataObject.MoveData;
import com.kodilla.model.dataObject.CheckedMovement;
import com.kodilla.model.stateOfTheGame.Game;
import com.kodilla.model.playerMoveCorrectness.MovingThePiece;

public class HumanMove extends PlayerMove {

    private MovingThePiece movingThePiece;

    public HumanMove(Game game, MovingThePiece movingThePiece) {
        super(game);
        this.movingThePiece = movingThePiece;
    }

    public CheckedMovement checkPlayerMove(MoveData moveData) {
        Pawn pawn = game.findPawn(moveData.getX(), moveData.getY());
        CheckedMovement checkedMovement = movingThePiece.checkingTheCorrectnessOfTheMovement(moveData.getNextX(), moveData.getNextY(), pawn, game.getGameBoard());
        doAllBehaviour(moveData, pawn, checkedMovement);
        checkedMovement.setPawn(pawn);
        return checkedMovement;
    }
}
