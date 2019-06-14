package com.kodilla.controller;
import com.kodilla.model.dataObject.MoveData;
import com.kodilla.model.dataObject.CheckedMovement;
import com.kodilla.model.Model;
import com.kodilla.view.CheckersApp;

public class Controller {
    private CheckersApp view;
    private Model model;

    public Controller(CheckersApp view) {
        this.view = view;
        model = new Model();
    }

    public void createStartBoard() {
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

    public void computerAndPlayer(MoveData moveData) {
        CheckedMovement checkedMovement = model.checkPlayerMovement(moveData);
        if (checkedMovement.isCorrect()) {
            test(moveData, checkedMovement);
            if (checkedMovement.isEndOfRound()) {
                view.setWinLabel(model.isWin());
                if(model.isWin().equals("")) {
                    CheckedMovement computerCheckedMove = model.computerMovement();
                    test(computerCheckedMove.getMoveData(), computerCheckedMove);
                    view.setWinLabel(model.isWin());
                }
            }
        } else {
            view.move(moveData.getX(), moveData.getY(), moveData.getX(), moveData.getY());
        }
    }

    private void test(MoveData moveData, CheckedMovement checkedMovement) {
        view.removeImageViewPawns(checkedMovement.getPositionsKilledPawns(), checkedMovement.getPawn().isBlack());
        if (checkedMovement.doQueen()) {
            view.changePawnIntoAQueen(moveData.getX(), moveData.getY(), checkedMovement.getPawn().isBlack());
        }
        view.move(moveData.getX(), moveData.getY(), moveData.getNextX(), moveData.getNextY());
    }

    public void testMainMenu() {
        view.removeMainMenu();
        createStartBoard();
        view.setDisablePawns(true, true);
    }

    public void doMove(MoveData moveData, boolean twoPlayers) {
        if (twoPlayers) {
            twoPlayers(moveData);
        } else {
            computerAndPlayer(moveData);
        }
    }

    private void twoPlayers(MoveData moveData) {
        CheckedMovement checkedMovement = model.checkPlayerMovement(moveData);
        if (checkedMovement.isCorrect()) {
            test(moveData, checkedMovement);
            if (checkedMovement.isEndOfRound()) {
                view.changeStatus();
                view.setDisablePawns(checkedMovement.getPawn().isBlack(), true);
                view.setDisablePawns(!checkedMovement.getPawn().isBlack(), false);
            }
        } else {
            view.move(moveData.getX(), moveData.getY(), moveData.getX(), moveData.getY());
        }
    }

    private void win() {
        if (model.isWin().equals("")) {
            view.setDisablePawns(true, true);
            view.setDisablePawns(false, true);
            view.setWinLabel(model.isWin());
        }
    }

    public void backToMenu() {
        view.clear();
        view.createMainMenu();
        model.doDefaultBoardGame();
    }
}
