package com.kodilla.checkersExperience;

import com.kodilla.myEnum.BoardSize;

import java.util.function.IntFunction;

public class Coordinates {
    private int x;
    private int y;
    private int nextX;
    private int nextY;
    private IntFunction<Integer> calculateX;
    private IntFunction<Integer> calculateY;

    public Coordinates(IntFunction<Integer> calculateX, IntFunction<Integer> calculateY) {
        this.calculateX = calculateX;
        this.calculateY = calculateY;
    }

    public void setX_Y(int x, int y){
        this.x = x;
        this.y = y;
    }

    public BoardSize calculate(){
        x = calculateX.apply(x);
        y = calculateY.apply(y);
        nextX = calculateX.apply(x);
        nextY = calculateY.apply(y);
        return correctAll();
    }

    private BoardSize correctAll() {
        if(isCorrect(x) && isCorrect(y) && isCorrect(nextX) && isCorrect(nextY)){
            return BoardSize.IS_OK;
        } else if (!isCorrect(x) || !isCorrect(y)){
            return BoardSize.XY_OVERSIZE;
        } else {
            return BoardSize.NEXT_XY_OVERSIZE;
        }
    }

    public void setCalculateX(IntFunction<Integer> calculateX) {
        this.calculateX = calculateX;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getNextX() {
        return nextX;
    }

    public int getNextY() {
        return nextY;
    }

    public IntFunction<Integer> getCalculateX() {
        return calculateX;
    }

    public IntFunction<Integer> getCalculateY() {
        return calculateY;
    }

    public boolean isCorrect(int point) {
        return (point >= 0 && point <= 7);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates that = (Coordinates) o;

        if (getCalculateX() != null ? !getCalculateX().equals(that.getCalculateX()) : that.getCalculateX() != null)
            return false;
        return getCalculateY() != null ? getCalculateY().equals(that.getCalculateY()) : that.getCalculateY() == null;

    }
}
