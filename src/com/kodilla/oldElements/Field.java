package com.kodilla.oldElements;

import com.kodilla.view.CheckersApp;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Field extends Rectangle {
    private Pawn pawn;
    private int x;
    private int y;

    public Field(boolean colour, int x, int y) {
        this.setWidth(CheckersApp.FIELD_SIZE);
        this.setHeight(CheckersApp.FIELD_SIZE);
        this.relocate(x * CheckersApp.FIELD_SIZE, y * CheckersApp.FIELD_SIZE);
        this.setFill(colour ? Color.WHITE : Color.BLACK);
        this.x = x;
        this.y = y;
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        Field field = new Field((x + y) % 2 == 0, x, y);
        Pawn pawn = hasPawn() ? ((Pawn) this.pawn.clone()) : null;
        field.setPawn(pawn);
        return field;
    }
}
