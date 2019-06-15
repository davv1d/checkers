package com.kodilla.model.userMoves;

import com.kodilla.model.boardBeaviour.ActionsOnTheBoard;
import com.kodilla.model.elementsOfTheBoard.Pawn;
import com.kodilla.model.dataObject.MoveData;
import com.kodilla.model.dataObject.ModelData;
import com.kodilla.model.stateOfTheGame.MainBoard;
import com.kodilla.model.playerMoveCorrectness.MovingThePiece;

public class HumanMove extends PlayerMove {

    private MovingThePiece movingThePiece;

    public HumanMove(MainBoard mainBoard, MovingThePiece movingThePiece) {
        super(mainBoard);
        this.movingThePiece = movingThePiece;
    }

    public ModelData checkPlayerMove(MoveData moveData) {
        Pawn pawn = ActionsOnTheBoard.findPawn(mainBoard.getGameBoard(),moveData.getX(), moveData.getY());
        ModelData modelData = movingThePiece.checkingTheCorrectnessOfTheMovement(moveData.getNextX(), moveData.getNextY(), pawn, mainBoard.getGameBoard());
        doAllBehaviour(moveData, pawn, modelData);
        modelData.setPawn(pawn);
        return modelData;
    }
}
