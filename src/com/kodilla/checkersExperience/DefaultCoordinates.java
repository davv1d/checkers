package com.kodilla.checkersExperience;

import java.util.function.IntFunction;

public class DefaultCoordinates {

    public static final IntFunction<Integer> addToPosition = p -> p + 1;
    public static final IntFunction<Integer> subtractFromPosition = p -> p - 1;
    public static Coordinates addX_addY = new Coordinates(addToPosition, addToPosition);
    public static Coordinates subX_subY = new Coordinates(subtractFromPosition, subtractFromPosition);
    public static Coordinates addX_subY = new Coordinates(addToPosition, subtractFromPosition);
    public static Coordinates subX_addY = new Coordinates(subtractFromPosition, addToPosition);

}
