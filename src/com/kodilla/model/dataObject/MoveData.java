package com.kodilla.model.dataObject;

import com.kodilla.model.elementsOfTheBoard.Position;

public class MoveData extends Position {
    private int nextX;
    private int nextY;

    public MoveData(int lastX, int lastY, int nextX, int nextY) {
        super(lastX, lastY);
        this.nextX = nextX;
        this.nextY = nextY;
    }

    public int getNextX() {
        return nextX;
    }

    public int getNextY() {
        return nextY;
    }
}
