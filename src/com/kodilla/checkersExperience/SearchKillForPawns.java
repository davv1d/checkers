package com.kodilla.checkersExperience;

import com.kodilla.myEnum.KindOfPosition;
import com.kodilla.testFindallMoves.Position;
import com.kodilla.tree.Node;
import com.kodilla.view.Field;
import com.kodilla.view.Pawn;

import java.util.ArrayList;
import java.util.List;

import static com.kodilla.checkersExperience.DefaultCoordinates.*;

public class SearchKillForPawns {


    public static boolean searchKillForPawn(Pawn pawn, Field[][] board, List<PawnAndPositions> allPawnsAndPositions) {
        List<PawnAndPositions> tempPawnAndPositions = new ArrayList<>();
        StartValues startValues = new StartValues(pawn, board).invoke();
        Node<PositionAndKilledPawn> root = startValues.getRoot();
        ElementsOfBoard elements = startValues.getElements();
        searchPawnKill(root, elements);
        List<Node<PositionAndKilledPawn>> leafNodes = new ArrayList<>();
        LastNodesAndCompactedPAwns.findLeafNode(root, leafNodes);
        CreatePathFromTheTree.createPath(pawn, leafNodes, tempPawnAndPositions);
        if (tempPawnAndPositions.isEmpty()) {
            return false;
        } else {
            allPawnsAndPositions.addAll(tempPawnAndPositions);
            return true;
        }
    }

    private static void searchPawnKill(Node<PositionAndKilledPawn> lastNode, ElementsOfBoard elements) {
        if (elements.y <= 1) {
            firstRectangleYSmallerOrEqual1(lastNode, elements);
        } else if (elements.y >= 2 && elements.y <= 5) {
            secondRectangleYBetween2And5(lastNode, elements);
        } else {
            thirdRectangleYBiggerOrEqual6(lastNode, elements);
        }
    }


    private static void firstRectangleYSmallerOrEqual1(Node<PositionAndKilledPawn> lastNode, ElementsOfBoard elements) {
        firstAndSecondRectangle(lastNode, elements, addX_addY, subX_addY);
    }

    private static void firstAndSecondRectangle(Node<PositionAndKilledPawn> lastNode, ElementsOfBoard elements, Coordinates coordinates1, Coordinates coordinates2) {
        if (elements.x <= 1) {
            canPawnKill(lastNode, coordinates1, elements);
        } else if (elements.x >= 2 && elements.x <= 5) {
            canPawnKill(lastNode, coordinates2, elements);
            canPawnKill(lastNode, coordinates1, elements);
        } else {
            canPawnKill(lastNode, coordinates2, elements);
        }
    }

    private static void thirdRectangleYBiggerOrEqual6(Node<PositionAndKilledPawn> lastNode, ElementsOfBoard elements) {
        firstAndSecondRectangle(lastNode, elements, addX_subY, subX_subY);
    }

    private static void secondRectangleYBetween2And5(Node<PositionAndKilledPawn> lastNode, ElementsOfBoard elements) {
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


    private static void canPawnKill(Node<PositionAndKilledPawn> lastNode, Coordinates coordinates, ElementsOfBoard elements) {
        coordinates.setX_Y(elements.x, elements.y);
        coordinates.calculate();
        ElementsOfBoard elementsOfBoard = new ElementsOfBoard(elements.pawn, elements.board, coordinates.getNextX(), coordinates.getNextY());
        Field nextStepField = elements.board[coordinates.getNextX()][coordinates.getNextY()];
        Field fieldToJump = elements.board[coordinates.getX()][coordinates.getY()];
        List<Pawn> compactedPawns = LastNodesAndCompactedPAwns.findCompactedPawns(lastNode);
        if (!nextStepField.hasPawn() || nextStepField.getPawn().equals(elements.pawn))
            if (fieldToJump.hasPawn() && !compactedPawns.contains(fieldToJump.getPawn()))
                if (!fieldToJump.getPawn().getPawnType().equals(elements.pawn.getPawnType())) {
                    Position emptyPosition = new Position(coordinates.getNextX(), coordinates.getNextY());
                    PositionAndKilledPawn newPosition = new PositionAndKilledPawn(emptyPosition, fieldToJump.getPawn(), KindOfPosition.NONE);
                    Node<PositionAndKilledPawn> node = new Node<>(newPosition);
                    lastNode.addChild(node);
                    searchPawnKill(node, elementsOfBoard);
                }
    }
}
