package com.kodilla.model.elementsOfTheBoard;

public class Field {
    private Pawn pawn;
    private boolean isBlack;

    public Field(boolean isBlack) {
        this.pawn = null;
        this.isBlack = isBlack;
    }

    public boolean isBlack() {
        return isBlack;
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
    public String toString() {
        return "Field{" +
                "pawn=" + pawn +
                ", isBlack=" + isBlack +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Field field = new Field(isBlack);
        Pawn pawn = hasPawn() ? ((Pawn) this.pawn.clone()) : null;
        field.setPawn(pawn);
        return field;
    }
}
