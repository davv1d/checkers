package com.kodilla.model.computerTest;

import com.kodilla.model.movementCalculation.MoveOfPawn;
import com.kodilla.model.elementsOfTheBoard.Position;

public class CalculatePoint {

    public static int calculatePoint(MoveOfPawn moveOfPawn){
        int point = 0;
        Position lastPosition = moveOfPawn.getLastPosition();
        if(lastPosition.getX()<6 && lastPosition.getX() > 1){
            point = point + 1;
        }
        point += moveOfPawn.getAmountKill() * 10;
        return point;
    }
}
