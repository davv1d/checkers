package com.kodilla.model.movementCalculation.pawn;

import com.kodilla.model.movementCalculation.Coordinates;
import com.kodilla.model.movementCalculation.MoveOfPawn;
import com.kodilla.model.movementCalculation.OneStepMove;
import com.kodilla.model.dataObject.ItemsOfBoard;
import com.kodilla.model.dataObject.StartValues;
import com.kodilla.model.constantly.KindOfPosition;
import com.kodilla.model.elementsOfTheBoard.Position;
import com.kodilla.model.tree.TreePath;
import com.kodilla.model.tree.Node;
import com.kodilla.model.tree.ActionsOnTheTree;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;

import java.util.ArrayList;
import java.util.List;

import static com.kodilla.model.constantly.DefaultCoordinates.*;

public class SearchKillForPawns {


    public static boolean searchKillForPawn(Pawn pawn, Field[][] board, List<MoveOfPawn> allPawnsAndPositions) {
        List<MoveOfPawn> tempPawnAndPositions = new ArrayList<>();
        StartValues startValues = new StartValues(pawn, board).invoke();
        Node<OneStepMove> root = startValues.getRoot();
        ItemsOfBoard elements = startValues.getElements();
        searchPawnKill(root, elements);
        List<Node<OneStepMove>> leafNodes = new ArrayList<>();
        ActionsOnTheTree.findLeafNode(root, leafNodes);
        TreePath.createPath(pawn, leafNodes, tempPawnAndPositions);
        if (tempPawnAndPositions.isEmpty()) {
            return false;
        } else {
            allPawnsAndPositions.addAll(tempPawnAndPositions);
            return true;
        }
    }

    private static void searchPawnKill(Node<OneStepMove> lastNode, ItemsOfBoard items) {
        if (items.getY() <= 1) {
            firstRectangleYSmallerOrEqual1(lastNode, items);
        } else if (items.getY() >= 2 && items.getY() <= 5) {
            secondRectangleYBetween2And5(lastNode, items);
        } else {
            thirdRectangleYBiggerOrEqual6(lastNode, items);
        }
    }


    private static void firstRectangleYSmallerOrEqual1(Node<OneStepMove> lastNode, ItemsOfBoard items) {
        firstAndSecondRectangle(lastNode, items, addX_addY, subX_addY);
    }

    private static void firstAndSecondRectangle(Node<OneStepMove> lastNode, ItemsOfBoard items, Coordinates coordinates1, Coordinates coordinates2) {
        if (items.getX() <= 1) {
            canPawnKill(lastNode, coordinates1, items);
        } else if (items.getX() >= 2 && items.getX() <= 5) {
            canPawnKill(lastNode, coordinates2, items);
            canPawnKill(lastNode, coordinates1, items);
        } else {
            canPawnKill(lastNode, coordinates2, items);
        }
    }

    private static void thirdRectangleYBiggerOrEqual6(Node<OneStepMove> lastNode, ItemsOfBoard items) {
        firstAndSecondRectangle(lastNode, items, addX_subY, subX_subY);
    }

    private static void secondRectangleYBetween2And5(Node<OneStepMove> lastNode, ItemsOfBoard items) {
        if (items.getX() <= 1) {
            canPawnKill(lastNode, addX_subY, items);
            canPawnKill(lastNode, addX_addY, items);
        } else if (items.getX() >= 2 && items.getX() <= 5) {
            canPawnKill(lastNode, addX_subY, items);
            canPawnKill(lastNode, addX_addY, items);
            canPawnKill(lastNode, subX_addY, items);
            canPawnKill(lastNode, subX_subY, items);
        } else {
            canPawnKill(lastNode, subX_subY, items);
            canPawnKill(lastNode, subX_addY, items);
        }
    }


    private static void canPawnKill(Node<OneStepMove> lastNode, Coordinates coordinates, ItemsOfBoard items) {
        coordinates.setX_Y(items.getX(), items.getY());
        coordinates.calculate();
        ItemsOfBoard itemsOfBoard = new ItemsOfBoard(items.getPawn(), items.getBoard(), coordinates.getNextX(), coordinates.getNextY());
        Field nextStepField = items.getBoard()[coordinates.getNextX()][coordinates.getNextY()];
        Field fieldToJump = items.getBoard()[coordinates.getX()][coordinates.getY()];
        List<Pawn> compactedPawns = ActionsOnTheTree.findCompactedPawns(lastNode);
        if (!nextStepField.hasPawn() || nextStepField.getPawn().equals(items.getPawn()))
            if (fieldToJump.hasPawn() && !compactedPawns.contains(fieldToJump.getPawn()))
                if (!fieldToJump.getPawn().isBlack() == items.getPawn().isBlack()) {
                    Position emptyPosition = new Position(coordinates.getNextX(), coordinates.getNextY());
                    OneStepMove newPosition = new OneStepMove(emptyPosition, fieldToJump.getPawn(), KindOfPosition.NONE);
                    Node<OneStepMove> node = new Node<>(newPosition);
                    lastNode.addChild(node);
                    searchPawnKill(node, itemsOfBoard);
                }
    }
}
