package com.kodilla.controller;

import com.kodilla.model.dataObject.MoveData;
import com.kodilla.model.dataObject.ModelData;
import com.kodilla.model.Model;
import com.kodilla.view.CheckersApp;

import java.util.function.Consumer;

public class Controller {
    private CheckersApp view;
    private Model model;

    public Controller(CheckersApp view) {
        this.view = view;
        model = new Model();
    }

    private void createStartBoard() {
        view.paintFields(model.getDefaultSettings().getFieldsBoard());
        view.paintPawns(model.getDefaultSettings().getPawnsBoard());
        view.createSideMenu();
    }

    public void newGame() {
        model.doDefaultBoardGame();
        view.clearLabels();
        view.removePawnsFromTheBoard();
        view.paintPawns(model.getDefaultSettings().getPawnsBoard());
        view.setDisablePawns(false, false);
        view.setDisablePawns(true, true);
    }

    private void actionInView(MoveData moveData, ModelData modelData) {
        view.removeImageViewPawns(modelData.getPositionsKilledPawns(), modelData.getPawn().isBlack());
        if (modelData.doQueen()) {
            view.changePawnIntoAQueen(moveData.getX(), moveData.getY(), modelData.getPawn().isBlack());
        }
        view.move(moveData.getX(), moveData.getY(), moveData.getNextX(), moveData.getNextY());
    }

    public void createMainMenu() {
        view.removeMainMenu();
        createStartBoard();
        view.setDisablePawns(true, true);
    }

    public void play(MoveData moveData, boolean twoPlayers) {
        if (twoPlayers) {
            makingTheMove(moveData, this::anotherPlayer);
        } else {
            makingTheMove(moveData, this::computerMove);
        }
    }

    private void makingTheMove(MoveData moveData, Consumer<Boolean> playerOrComputer) {
        ModelData modelData = model.checkPlayerMovement(moveData);
        if (modelData.isTheRightMove()) {
            actionInView(moveData, modelData);
            if (modelData.isEndOfRound()) {
                if (!modelData.isWinner()) {
                    playerOrComputer.accept(modelData.getPawn().isBlack());
                } else {
                    view.winner(modelData.getWinner());
                }
            }
        } else {
            view.move(moveData.getX(), moveData.getY(), moveData.getX(), moveData.getY());
        }
    }

    private void computerMove(boolean isBlack) {
        ModelData computerCheckedMove = model.computerMovement();
        actionInView(computerCheckedMove.getMoveData(), computerCheckedMove);
        if (computerCheckedMove.isWinner()) {
            view.winner(computerCheckedMove.getWinner());
        }
    }

    private void anotherPlayer(boolean isBlack) {
        view.changeStatus(isBlack);
        view.setDisablePawns(isBlack, true);
        view.setDisablePawns(!isBlack, false);

    }

    public void backToMenu() {
        view.clear();
        view.createMainMenu();
        model.doDefaultBoardGame();
    }
}
