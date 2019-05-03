package com.kodilla.view;

import javafx.scene.image.ImageView;

public class Pawn extends ImageView {
    private PawnType type;
    private boolean isQueen;
    private double lastPositionX, lastPositionY;
    private double mouseX, mouseY;

    public Pawn(String url, PawnType type, int x, int y) {
        super(url);
        this.type = type;
        this.isQueen = false;
        this.setFitHeight(CheckersApp.PAWN_SIZE);
        this.setFitWidth(CheckersApp.PAWN_SIZE);
        this.move(x, y);
        int positionInCentre = (CheckersApp.FIELD_SIZE - CheckersApp.PAWN_SIZE) / 2;
        this.setTranslateX(positionInCentre);
        this.setTranslateY(positionInCentre);
        this.mouseEvent();
    }

    public void move(int x, int y) {
        lastPositionX = x * CheckersApp.FIELD_SIZE;
        lastPositionY = y * CheckersApp.FIELD_SIZE;
        relocate(lastPositionX, lastPositionY);
    }

    private void mouseEvent() {
        this.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });
        this.setOnMouseDragged(event -> {
            relocate(event.getSceneX() - mouseX + lastPositionX, event.getSceneY() - mouseY + lastPositionY);
        });
    }

    public PawnType getPlayer() {
        return type;
    }

    public boolean isQueen() {
        return isQueen;
    }

    public double getLastPositionX() {
        return lastPositionX;
    }

    public double getLastPositionY() {
        return lastPositionY;
    }

    public void setQueen(boolean queen) {
        isQueen = queen;
    }

    public void setLastPositionX(double lastPositionX) {
        this.lastPositionX = lastPositionX;
    }

    public void setLastPositionY(double lastPositionY) {
        this.lastPositionY = lastPositionY;
    }
}
