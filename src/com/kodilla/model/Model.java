package com.kodilla.model;

import com.kodilla.model.computerTest.Computer;
import com.kodilla.model.constantly.Win;
import com.kodilla.model.dataObject.CheckedMovement;
import com.kodilla.model.stateOfTheGame.DefaultSettings;
import com.kodilla.model.stateOfTheGame.Game;
import com.kodilla.model.userMoves.ComputerMove;
import com.kodilla.model.userMoves.HumanMove;
import com.kodilla.model.dataObject.MoveData;
import com.kodilla.model.playerMoveCorrectness.MovingThePiece;

public class Model {
    private Game game;
    private DefaultSettings defaultSettings;
    private MovingThePiece movingThePiece;
    private HumanMove humanMove;
    private ComputerMove computerMove;

    public Model() {
        defaultSettings = new DefaultSettings();
        game = new Game(defaultSettings.getLogicBoard());
        movingThePiece = new MovingThePiece();
        humanMove = new HumanMove(game, movingThePiece);
        Computer computer = new Computer();
        computerMove = new ComputerMove(game, computer);
    }

    public void doDefaultBoardGame() {
        game.setGameBoard(defaultSettings.getLogicBoard());
    }

    public DefaultSettings getDefaultSettings() {
        return defaultSettings;
    }

    public CheckedMovement checkPlayerMovement(MoveData moveData) {
        return humanMove.checkPlayerMove(moveData);
    }

    public CheckedMovement computerMovement(){
        return computerMove.checkComputerMove();
    }

    public String isWin() {
        boolean blackPawns = game.isPawnsOfASpecificType(true);
        boolean whitePawns = game.isPawnsOfASpecificType(false);
        if (blackPawns && !whitePawns) {
            return "White pawns win";
        } else if (!blackPawns && whitePawns) {
            return "Black pawns win";
        } else {
            return "";
        }
    }
}
