package com.kodilla.constantly;

import com.kodilla.movementCalculation.Coordinates;

import java.util.function.IntFunction;

public class DefaultCoordinates {

    public static final IntFunction<Integer> addToPosition = p -> p + 1;
    public static final IntFunction<Integer> subtractFromPosition = p -> p - 1;
    public static final Coordinates addX_addY = new Coordinates(addToPosition, addToPosition);
    public static final Coordinates subX_subY = new Coordinates(subtractFromPosition, subtractFromPosition);
    public static final Coordinates addX_subY = new Coordinates(addToPosition, subtractFromPosition);
    public static final Coordinates subX_addY = new Coordinates(subtractFromPosition, addToPosition);

}
