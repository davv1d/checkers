package com.kodilla.view;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CheckersApp extends Application {

    public static final int FIELD_SIZE = 80;
    public static final int WIDTH = 8;
    public static final int HEIGTH = 8;
    public static final int PAWN_SIZE = 50;
    public static final String URL_IMAGE_BLACK_PAWN = "file:resources/goldPawn.png";
    public static final String URL_IMAGE_WHITE_PAWN = "file:resources/silverPawn.png";

    private Group fieldGroup = new Group();
    private Group pawnGroup = new Group();
    private Field[][] board = new Field[HEIGTH][WIDTH];
    private Game game = new Game(board);
    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(FIELD_SIZE * WIDTH, FIELD_SIZE * HEIGTH);
        root.getChildren().addAll(fieldGroup, pawnGroup);
        for (int y = 0; y < HEIGTH; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Field field = new Field((x + y) % 2 == 0, x, y);
                board[y][x] = field;
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
                    pawnGroup.getChildren().add(pawn);
                }
            }
        }
        return root;
    }

    private Pawn makePawn(String url, PawnType type, int x, int y) {
        Pawn pawn = new Pawn(url, type, x, y);
        pawn.setOnMouseReleased(event -> {
            int newX = (int) event.getSceneX() / FIELD_SIZE;
            int newY = (int) event.getSceneY() / FIELD_SIZE;
            game.tryMove(newX, newY, (Pawn) event.getSource());
//            if ((newX + newY) % 2 != 0) {
//                pawn.move(newX, newY);
//            } else {
//                board[2][1].getPawn().move(1, 3);
//                pawn.move((int) pawn.getLastPositionX() / FIELD_SIZE, (int) pawn.getLastPositionY() / FIELD_SIZE);
//            }
        });
        return pawn;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Checkers");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
