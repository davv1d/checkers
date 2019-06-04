package com.kodilla.view;

import com.kodilla.myEnum.PawnType;
import javafx.scene.image.ImageView;

public class Pawn extends ImageView {
    private String url;
    private PawnType type;
    private boolean isQueen;
    private int lastPositionX, lastPositionY;
    private double mouseX, mouseY;

    public Pawn(String url, PawnType type, int x, int y) {
        super(url);
        this.url = url;
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
        lastPositionX = x;
        lastPositionY = y;
        relocate(lastPositionX * CheckersApp.FIELD_SIZE, lastPositionY * CheckersApp.FIELD_SIZE);
    }

    private void mouseEvent() {
        this.setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });
        this.setOnMouseDragged(event -> {
            relocate(event.getSceneX() - mouseX + lastPositionX * CheckersApp.FIELD_SIZE, event.getSceneY() - mouseY + lastPositionY * CheckersApp.FIELD_SIZE);
        });
    }

    public String getUrl() {
        return url;
    }

    public PawnType getPawnType() {
        return type;
    }

    public boolean isQueen() {
        return isQueen;
    }

    public int getLastPositionX() {
        return lastPositionX;
    }

    public int getLastPositionY() {
        return lastPositionY;
    }

    public void setType(PawnType type) {
        this.type = type;
    }

    public void setQueen(boolean queen) {
        isQueen = queen;
    }

    public void setLastPositionX(int lastPositionX) {
        this.lastPositionX = lastPositionX;
    }

    public void setLastPositionY(int lastPositionY) {
        this.lastPositionY = lastPositionY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pawn pawn = (Pawn) o;

        if (isQueen() != pawn.isQueen()) return false;
        if (getLastPositionX() != pawn.getLastPositionX()) return false;
        if (getLastPositionY() != pawn.getLastPositionY()) return false;
        return type == pawn.type;

    }

    @Override
    public String toString() {
        return "{" + type +
                ", X=" + lastPositionX +
                ", Y=" + lastPositionY +
                " isQueen= " + isQueen +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Pawn pawn = new Pawn(this.url, this.type, lastPositionX, lastPositionY);
        pawn.setQueen(this.isQueen);
        return pawn;
    }
}
