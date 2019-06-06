package com.kodilla.movementCalculation.pawn;

import com.kodilla.movementCalculation.Coordinates;
import com.kodilla.movementCalculation.PawnAndPositions;
import com.kodilla.movementCalculation.PositionAndKilledPawn;
import com.kodilla.movementCalculation.dto.ElementsOfBoardDto;
import com.kodilla.movementCalculation.dto.StartValues;
import com.kodilla.constantly.KindOfPosition;
import com.kodilla.oldElements.Position;
import com.kodilla.tree.CreatePathFromTheTree;
import com.kodilla.tree.Node;
import com.kodilla.tree.LastNodesAndCompactedPawns;
import com.kodilla.oldElements.Field;
import com.kodilla.oldElements.Pawn;

import java.util.ArrayList;
import java.util.List;

import static com.kodilla.constantly.DefaultCoordinates.*;

public class SearchKillForPawns {


    public static boolean searchKillForPawn(Pawn pawn, Field[][] board, List<PawnAndPositions> allPawnsAndPositions) {
        List<PawnAndPositions> tempPawnAndPositions = new ArrayList<>();
        StartValues startValues = new StartValues(pawn, board).invoke();
        Node<PositionAndKilledPawn> root = startValues.getRoot();
        ElementsOfBoardDto elements = startValues.getElements();
        searchPawnKill(root, elements);
        List<Node<PositionAndKilledPawn>> leafNodes = new ArrayList<>();
        LastNodesAndCompactedPawns.findLeafNode(root, leafNodes);
        CreatePathFromTheTree.createPath(pawn, leafNodes, tempPawnAndPositions);
        if (tempPawnAndPositions.isEmpty()) {
            return false;
        } else {
            allPawnsAndPositions.addAll(tempPawnAndPositions);
            return true;
        }
    }

    private static void searchPawnKill(Node<PositionAndKilledPawn> lastNode, ElementsOfBoardDto elements) {
        if (elements.y <= 1) {
            firstRectangleYSmallerOrEqual1(lastNode, elements);
        } else if (elements.y >= 2 && elements.y <= 5) {
            secondRectangleYBetween2And5(lastNode, elements);
        } else {
            thirdRectangleYBiggerOrEqual6(lastNode, elements);
        }
    }


    private static void firstRectangleYSmallerOrEqual1(Node<PositionAndKilledPawn> lastNode, ElementsOfBoardDto elements) {
        firstAndSecondRectangle(lastNode, elements, addX_addY, subX_addY);
    }

    private static void firstAndSecondRectangle(Node<PositionAndKilledPawn> lastNode, ElementsOfBoardDto elements, Coordinates coordinates1, Coordinates coordinates2) {
        if (elements.x <= 1) {
            canPawnKill(lastNode, coordinates1, elements);
        } else if (elements.x >= 2 && elements.x <= 5) {
            canPawnKill(lastNode, coordinates2, elements);
            canPawnKill(lastNode, coordinates1, elements);
        } else {
            canPawnKill(lastNode, coordinates2, elements);
        }
    }

    private static void thirdRectangleYBiggerOrEqual6(Node<PositionAndKilledPawn> lastNode, ElementsOfBoardDto elements) {
        firstAndSecondRectangle(lastNode, elements, addX_subY, subX_subY);
    }

    private static void secondRectangleYBetween2And5(Node<PositionAndKilledPawn> lastNode, ElementsOfBoardDto elements) {
        if (elements.x <= 1) {
            canPawnKill(lastNode, addX_subY, elements);
            canPawnKill(lastNode, addX_addY, elements);
        } else if (elements.x >= 2 && elements.x <= 5) {
            canPawnKill(lastNode, addX_subY, elements);
            canPawnKill(lastNode, addX_addY, elements);
            canPawnKill(lastNode, subX_addY, elements);
            canPawnKill(lastNode, subX_subY, elements);
        } else {
            canPawnKill(lastNode, subX_subY, elements);
            canPawnKill(lastNode, subX_addY, elements);
        }
    }


    private static void canPawnKill(Node<PositionAndKilledPawn> lastNode, Coordinates coordinates, ElementsOfBoardDto elements) {
        coordinates.setX_Y(elements.x, elements.y);
        coordinates.calculate();
        ElementsOfBoardDto elementsOfBoardDto = new ElementsOfBoardDto(elements.pawn, elements.board, coordinates.getNextX(), coordinates.getNextY());
        Field nextStepField = elements.board[coordinates.getNextX()][coordinates.getNextY()];
        Field fieldToJump = elements.board[coordinates.getX()][coordinates.getY()];
        List<Pawn> compactedPawns = LastNodesAndCompactedPawns.findCompactedPawns(lastNode);
        if (!nextStepField.hasPawn() || nextStepField.getPawn().equals(elements.pawn))
            if (fieldToJump.hasPawn() && !compactedPawns.contains(fieldToJump.getPawn()))
                if (!fieldToJump.getPawn().getPawnType().equals(elements.pawn.getPawnType())) {
                    Position emptyPosition = new Position(coordinates.getNextX(), coordinates.getNextY());
                    PositionAndKilledPawn newPosition = new PositionAndKilledPawn(emptyPosition, fieldToJump.getPawn(), KindOfPosition.NONE);
                    Node<PositionAndKilledPawn> node = new Node<>(newPosition);
                    lastNode.addChild(node);
                    searchPawnKill(node, elementsOfBoardDto);
                }
    }
}
