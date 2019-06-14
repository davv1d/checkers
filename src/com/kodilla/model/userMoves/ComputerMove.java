package com.kodilla.model.userMoves;

import com.kodilla.model.computerTest.Computer;
import com.kodilla.model.dataObject.CheckedMovement;
import com.kodilla.model.stateOfTheGame.Game;


public class ComputerMove extends PlayerMove{

    private Computer computer;

    public ComputerMove(Game game, Computer computer) {
        super(game);
        this.computer = computer;
    }

    public CheckedMovement checkComputerMove() {
        CheckedMovement checkedMovement = computer.test(game.getGameBoard());
        doAllBehaviour(checkedMovement.getMoveData(), checkedMovement.getPawn(), checkedMovement);
        return checkedMovement;
    }
}
