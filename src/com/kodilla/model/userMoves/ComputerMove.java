package com.kodilla.model.userMoves;

import com.kodilla.model.computer.Computer;
import com.kodilla.model.dataObject.ModelData;
import com.kodilla.model.stateOfTheGame.MainBoard;


public class ComputerMove extends PlayerMove{

    private Computer computer;

    public ComputerMove(MainBoard mainBoard, Computer computer) {
        super(mainBoard);
        this.computer = computer;
    }

    public ModelData checkComputerMove() {
        ModelData modelData = computer.computerMove(mainBoard.getGameBoard());
        doAllBehaviour(modelData.getMoveData(), modelData.getPawn(), modelData);
        return modelData;
    }
}
