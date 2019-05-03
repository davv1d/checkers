package com.kodilla.view;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Field extends Rectangle {
    private Pawn pawn;

    public Field(boolean colour, int x, int y) {
        this.setWidth(CheckersApp.FIELD_SIZE);
        this.setHeight(CheckersApp.FIELD_SIZE);
        this.relocate(x * CheckersApp.FIELD_SIZE, y * CheckersApp.FIELD_SIZE);
        this.setFill(colour ? Color.WHITE : Color.BLACK);
    }

    public boolean hasPawn() {
        return pawn != null;
    }

    public Pawn getPawn() {
        return pawn;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }
}
