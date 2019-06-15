package com.kodilla.model.userMoves;

import com.kodilla.model.boardBeaviour.ActionsOnThePawns;
import com.kodilla.model.elementsOfTheBoard.Pawn;
import com.kodilla.model.dataObject.MoveData;
import com.kodilla.model.dataObject.ModelData;
import com.kodilla.model.stateOfTheGame.MainBoard;

abstract class PlayerMove {
    MainBoard mainBoard;

    PlayerMove(MainBoard mainBoard) {
        this.mainBoard = mainBoard;
    }

    void doAllBehaviour(MoveData moveData, Pawn pawn, ModelData modelData) {
        if (modelData.isTheRightMove()) {
            ActionsOnThePawns.removingKilledPawns(modelData.getPositionsKilledPawns(), mainBoard.getGameBoard());
            ActionsOnThePawns.realMove(moveData, pawn, mainBoard.getGameBoard());
            ActionsOnThePawns.doQueen(pawn, modelData.doQueen());
        }
    }
}
