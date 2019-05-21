package com.kodilla.view;

import com.kodilla.myEnum.PawnType;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.stream.Collectors;

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
    private Field[][] board = new Field[WIDTH][HEIGHT];
    private Game game;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(FIELD_SIZE * WIDTH, FIELD_SIZE * HEIGHT);
        root.getChildren().addAll(fieldGroup, pawnGroup);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Field field = new Field((x + y) % 2 == 0, x, y);
                board[x][y] = field;
                fieldGroup.getChildren().add(field);

                Pawn pawn = null;
                if (y <= 2 && (x + y) % 2 != 0) {
                    pawn = makePawn(URL_IMAGE_BLACK_PAWN, PawnType.BLACK, x, y);
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
            //i don't know
            pawnGroup.getChildren().removeAll(pawnGroup.getChildren().stream()
                    .filter(node -> !node.isVisible())
                    .collect(Collectors.toSet()));
        });
        return pawn;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        game = new Game(board, this);
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
