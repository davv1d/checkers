package com.kodilla.computerTest;

import com.kodilla.movementCalculation.PawnAndPositions;
import com.kodilla.oldElements.Position;

public class CalculatePoint {

    public static int calculatePoint(PawnAndPositions pawnAndPositions){
        int point = 0;
        Position lastPosition = pawnAndPositions.getLastPosition();
        if(lastPosition.getX()<6 && lastPosition.getX() > 1){
            point = point + 1;
        }
        point += pawnAndPositions.getAmountKill() * 10;
        return point;
    }
}
