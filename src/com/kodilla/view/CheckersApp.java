package com.kodilla.view;

import com.kodilla.model.constantly.Winner;
import com.kodilla.model.dataObject.MoveData;
import com.kodilla.controller.Controller;
import com.kodilla.model.elementsOfTheBoard.Position;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;

public class CheckersApp extends Application {

    static final int FIELD_SIZE = 80;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private static final int PAWN_SIZE = 50;
    private static final String URL_IMAGE_BLACK_PAWN = "file:resources/goldPawn.png";
    private static final String URL_IMAGE_WHITE_PAWN = "file:resources/silverPawn.png";

    private static final String URL_IMAGE_BLACK_QUEEN = "file:resources/goldQueen2.png";
    private static final String URL_IMAGE_WHITE_QUEEN = "file:resources/silverQueen2.png";
    private static final String WON_WHITE = "White pawns won";
    private static final String WON_BLACK = "Black pawns won";

    private Group fieldGroup = new Group();
    private Group sideMenuGroup = new Group();
    private Group mainMenuGroup = new Group();
    private Group blackPawnGroup = new Group();
    private Group whitePawnGroup = new Group();
    private Pane root = new Pane();
    private Button resetButton = new Button("New game");
    private Controller controller = new Controller(this);
    private ImageViewPawn[][] viewPawns = new ImageViewPawn[WIDTH][HEIGHT];
    private Label nameLabel = new Label("Checkers");
    private RadioButton twoPlayers = new RadioButton("2 players");
    private RadioButton computerAndPlayer = new RadioButton("Computer");
    private ToggleGroup answer = new ToggleGroup();
    private Button submit = new Button("PLAY");
    private String status = "White pawns move";
    private String status2 = "Black pawns move";
    private Label gameStatusLabel = new Label();
    private Label winLabel = new Label("");
    private Button backToMenu = new Button("Back to menu");

    @Override
    public void start(Stage primaryStage) throws Exception {
        createMainMenu();
        root.getChildren().addAll(fieldGroup, blackPawnGroup, whitePawnGroup, sideMenuGroup, mainMenuGroup);
        Scene scene = new Scene(root, FIELD_SIZE * WIDTH + 150, FIELD_SIZE * HEIGHT);
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void resetClick(MouseEvent mouseEvent) {
        controller.newGame();
    }

    private void submitMainMenu(MouseEvent event) {
        controller.createMainMenu();
    }

    private void backToMenuClick(MouseEvent event) {
        controller.backToMenu();
    }

    private void mouseReleased(MouseEvent event) {
        int newX = (int) event.getSceneX() / FIELD_SIZE;
        int newY = (int) event.getSceneY() / FIELD_SIZE;
        ImageViewPawn pawn = (ImageViewPawn) event.getSource();
        MoveData moveData = new MoveData(pawn.getLastPositionX(), pawn.getLastPositionY(), newX, newY);
        controller.play(moveData, twoPlayers.isSelected());

    }

    public void removePawnsFromTheBoard() {
        blackPawnGroup.getChildren().clear();
        whitePawnGroup.getChildren().clear();
        viewPawns = new ImageViewPawn[WIDTH][HEIGHT];
    }

    public void changeStatus(boolean isBlack) {
        if (isBlack) {
            gameStatusLabel.setText(status);
        } else {
            gameStatusLabel.setText(status2);
        }
    }

    public void clear() {
        fieldGroup.getChildren().clear();
        sideMenuGroup.getChildren().clear();
        blackPawnGroup.getChildren().clear();
        whitePawnGroup.getChildren().clear();
        viewPawns = new ImageViewPawn[WIDTH][HEIGHT];
    }

    public void clearLabels() {
        winLabel.setText("");
        gameStatusLabel.setText(status);
    }

    public void createMainMenu() {
        twoPlayers.relocate(FIELD_SIZE * 3, 250);
        computerAndPlayer.relocate(FIELD_SIZE * 3, 270);
        twoPlayers.setToggleGroup(answer);
        twoPlayers.setSelected(true);
        computerAndPlayer.setToggleGroup(answer);
        nameLabel.relocate(FIELD_SIZE * 3, 150);
        nameLabel.setFont(new Font("Arial", 30));
        submit.relocate(FIELD_SIZE * 3, 320);
        submit.setOnMouseClicked(this::submitMainMenu);
        mainMenuGroup.getChildren().addAll(twoPlayers, computerAndPlayer, nameLabel, submit);
    }

    public void removeMainMenu() {
        mainMenuGroup.getChildren().clear();
    }

    public void createSideMenu() {
        backToMenu.relocate(FIELD_SIZE * WIDTH + 15, 400);
        backToMenu.setOnMouseClicked(this::backToMenuClick);
        winLabel.setText("");
        winLabel.relocate(FIELD_SIZE * WIDTH + 15, 120);
        gameStatusLabel.setText(status);
        gameStatusLabel.relocate(FIELD_SIZE * WIDTH + 15, 60);
        resetButton.relocate(FIELD_SIZE * WIDTH + 15, 15);
        resetButton.setOnMouseClicked(this::resetClick);
        sideMenuGroup.getChildren().addAll(resetButton, gameStatusLabel, winLabel, backToMenu);
    }

    public void paintPawns(Boolean[][] pawnsBoard) {
        for (int x = 0; x < pawnsBoard.length; x++) {
            for (int y = 0; y < pawnsBoard[x].length; y++) {
                if (pawnsBoard[x][y] != null) {
                    createPawn(pawnsBoard[x][y], x, y);
                }
            }
        }
    }

    private void createPawn(boolean isBlack, int x, int y) {
        String url = isBlack ? URL_IMAGE_BLACK_PAWN : URL_IMAGE_WHITE_PAWN;
        ImageViewPawn pawn = new ImageViewPawn(url, x, y);
        viewPawns[x][y] = pawn;
        pawn.setFitHeight(CheckersApp.PAWN_SIZE);
        pawn.setFitWidth(CheckersApp.PAWN_SIZE);
        int positionInCentre = (CheckersApp.FIELD_SIZE - CheckersApp.PAWN_SIZE) / 2;
        pawn.setTranslateX(positionInCentre);
        pawn.setTranslateY(positionInCentre);
        pawn.relocate(x * CheckersApp.FIELD_SIZE, y * CheckersApp.FIELD_SIZE);
        pawn.setOnMouseReleased(this::mouseReleased);
        if (isBlack) {
            blackPawnGroup.getChildren().add(pawn);
        } else {
            whitePawnGroup.getChildren().add(pawn);
        }
    }

    public void winner(Winner winner) {
        setDisablePawns(true, true);
        setDisablePawns(false, true);
        gameStatusLabel.setText("");
        whoWon(winner);
    }

    private void whoWon(Winner winner) {
        switch (winner) {
            case WHITE:
                winLabel.setText(WON_WHITE);
                break;
            case BLACK:
                winLabel.setText(WON_BLACK);
                break;
            case NONE:
                winLabel.setText("");
                break;
        }
    }

    public void move(int lastPositionX, int lastPositionY, int newX, int newY) {
        ImageViewPawn imageViewPawn = viewPawns[lastPositionX][lastPositionY];
        imageViewPawn.setLastPositionX(newX);
        imageViewPawn.setLastPositionY(newY);
        viewPawns[lastPositionX][lastPositionY] = null;
        viewPawns[newX][newY] = imageViewPawn;
        imageViewPawn.relocate(newX * FIELD_SIZE, newY * FIELD_SIZE);
    }

    public void removeImageViewPawns(List<Position> positionsKilledPawns, boolean isBlack) {
        for (Position position : positionsKilledPawns) {
            removeImageViewPawn(position.getX(), position.getY(), isBlack);
        }
    }

    public void setDisablePawns(boolean isBlack, boolean isDisable) {
        if (isBlack) {
            blackPawnGroup.getChildren().forEach(node -> node.setDisable(isDisable));
        } else {
            whitePawnGroup.getChildren().forEach(node -> node.setDisable(isDisable));
        }
    }

    private void removeImageViewPawn(int x, int y, boolean isBlack) {
        ImageViewPawn imageViewPawn = viewPawns[x][y];
        viewPawns[x][y] = null;
        if (!isBlack) {
            blackPawnGroup.getChildren().remove(imageViewPawn);
        } else {
            whitePawnGroup.getChildren().remove(imageViewPawn);
        }
    }

    public void paintFields(boolean[][] fieldsBoard) {
        for (int x = 0; x < fieldsBoard.length; x++) {
            for (int y = 0; y < fieldsBoard[x].length; y++) {
                createField(fieldsBoard[x][y], x, y);
            }
        }
    }

    private void createField(boolean isBlack, int x, int y) {
        Rectangle rectangle = new Rectangle();
        rectangle.setWidth(CheckersApp.FIELD_SIZE);
        rectangle.setHeight(CheckersApp.FIELD_SIZE);
        rectangle.relocate(x * CheckersApp.FIELD_SIZE, y * CheckersApp.FIELD_SIZE);
        rectangle.setFill(isBlack ? Color.BLACK : Color.WHITE);
        fieldGroup.getChildren().add(rectangle);
    }

    public void changePawnIntoAQueen(int imagePawnX, int imagePawnY, boolean isBlack) {
        ImageViewPawn imageViewPawn = viewPawns[imagePawnX][imagePawnY];
        Image image;
        if (isBlack) {
            image = new Image(URL_IMAGE_BLACK_QUEEN);
        } else {
            image = new Image(URL_IMAGE_WHITE_QUEEN);
        }
        imageViewPawn.setImage(image);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
