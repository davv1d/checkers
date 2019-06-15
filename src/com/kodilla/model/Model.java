package com.kodilla.model;

import com.kodilla.model.computer.Computer;
import com.kodilla.model.dataObject.ModelData;
import com.kodilla.model.stateOfTheGame.DefaultSettings;
import com.kodilla.model.stateOfTheGame.MainBoard;
import com.kodilla.model.stateOfTheGame.Judge;
import com.kodilla.model.userMoves.ComputerMove;
import com.kodilla.model.userMoves.HumanMove;
import com.kodilla.model.dataObject.MoveData;
import com.kodilla.model.playerMoveCorrectness.MovingThePiece;

public class Model {
    private MainBoard mainBoard;
    private DefaultSettings defaultSettings;
    private HumanMove humanMove;
    private ComputerMove computerMove;

    public Model() {
        defaultSettings = new DefaultSettings();
        mainBoard = new MainBoard(defaultSettings.getLogicBoard());
        MovingThePiece movingThePiece = new MovingThePiece();
        humanMove = new HumanMove(mainBoard, movingThePiece);
        Computer computer = new Computer();
        computerMove = new ComputerMove(mainBoard, computer);
    }

    public void doDefaultBoardGame() {
        mainBoard.setGameBoard(defaultSettings.getLogicBoard());
    }

    public DefaultSettings getDefaultSettings() {
        return defaultSettings;
    }

    public ModelData checkPlayerMovement(MoveData moveData) {
        ModelData modelData = humanMove.checkPlayerMove(moveData);
        modelData.setWinner(Judge.checkWin(modelData.getPawn().isBlack(), mainBoard.getGameBoard()));
        return modelData;
    }

    public ModelData computerMovement() {
        ModelData modelData = computerMove.checkComputerMove();
        modelData.setWinner(Judge.checkWin(modelData.getPawn().isBlack(), mainBoard.getGameBoard()));
        return modelData;
    }
}
