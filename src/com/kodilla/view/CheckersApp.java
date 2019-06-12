package com.kodilla.view;

import com.kodilla.constantly.PawnType;
import com.kodilla.oldElements.Field;
import com.kodilla.oldElements.Game;
import com.kodilla.oldElements.Pawn;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class CheckersApp extends Application {

    public static final int FIELD_SIZE = 80;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    public static final int PAWN_SIZE = 50;
    public static final String URL_IMAGE_BLACK_PAWN = "file:resources/goldPawn.png";
    public static final String URL_IMAGE_WHITE_PAWN = "file:resources/silverPawn.png";

    public static final String URL_IMAGE_BLACK_QUEEN = "file:resources/goldQueen2.png";
    public static final String URL_IMAGE_WHITE_QUEEN = "file:resources/silverQueen2.png";

    private Group fieldGroup = new Group();
    private Group pawnGroup = new Group();
    private Group menuGroup = new Group();
    private Group playerPawnsGroup = new Group();
    private Group computerPawnsGroup = new Group();
    private Field[][] board = new Field[WIDTH][HEIGHT];
    private Game game;
    private Pane root = new Pane();
    private Button resetButton = new Button("New game");

    private void createContent() {
        root.setPrefSize(FIELD_SIZE * WIDTH, FIELD_SIZE * HEIGHT);
        resetButton.relocate(FIELD_SIZE * WIDTH + 15, 10);
        resetButton.setOnMouseClicked(this::resetClick);
        menuGroup.getChildren().addAll(resetButton);
        root.getChildren().addAll(fieldGroup, pawnGroup, menuGroup);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Field field = new Field((x + y) % 2 == 0, x, y);
                board[x][y] = field;
                fieldGroup.getChildren().add(field);

                Pawn pawn = null;
                if (y <= 2 && (x + y) % 2 != 0) {
                    pawn = makePawn(URL_IMAGE_BLACK_PAWN, PawnType.BLACK, x, y);
                    pawn.setDisable(true);
                }

                if (y >= 5 && (x + y) % 2 != 0) {
                    pawn = makePawn(URL_IMAGE_WHITE_PAWN, PawnType.WHITE, x, y);
                }
                if (pawn != null) {
                    field.setPawn(pawn);
                    if (pawn.getPawnType().equals(PawnType.WHITE)) {
                        playerPawnsGroup.getChildren().add(pawn);
                    } else {
                        computerPawnsGroup.getChildren().add(pawn);
                    }
                }
            }
        }
        pawnGroup.getChildren().addAll(playerPawnsGroup, computerPawnsGroup);
    }

    public void playerPawnBlock(boolean block) {
        playerPawnsGroup.getChildren().forEach(node -> {
            if (node != null) {
                node.setDisable(block);
            }
        });
    }

    private void resetClick(MouseEvent mouseEvent) {
        root.getChildren().clear();
        playerPawnsGroup.getChildren().clear();
        computerPawnsGroup.getChildren().clear();
        menuGroup.getChildren().clear();
        fieldGroup.getChildren().clear();
        pawnGroup.getChildren().clear();
        createContent();
    }

    private Pawn makePawn(String url, PawnType type, int x, int y) {
        Pawn pawn = new Pawn(url, type, x, y);
        pawn.setOnMouseReleased(event -> {
                int newX = (int) event.getSceneX() / FIELD_SIZE;
                int newY = (int) event.getSceneY() / FIELD_SIZE;
                game.tryMove(newX, newY, (Pawn) event.getSource(), board);
        });
        return pawn;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        createContent();
        Scene scene = new Scene(root, FIELD_SIZE * WIDTH + 100, FIELD_SIZE * HEIGHT);
        game = new Game(this);
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public void changePawnIntoAQueen(Pawn pawn) {
        Image image;
        if (pawn.getPawnType().equals(PawnType.WHITE)) {
            image = new Image(URL_IMAGE_WHITE_QUEEN);
        } else {
            image = new Image(URL_IMAGE_BLACK_QUEEN);
        }
        pawn.setImage(image);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
