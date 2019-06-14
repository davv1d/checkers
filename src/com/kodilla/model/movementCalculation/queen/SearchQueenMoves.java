package com.kodilla.model.movementCalculation.queen;

import com.kodilla.model.boardBeaviour.Conditions;
import com.kodilla.model.movementCalculation.Coordinates;
import com.kodilla.model.movementCalculation.MoveOfPawn;
import com.kodilla.model.movementCalculation.OneStepMove;
import com.kodilla.model.movementCalculation.QueenKill;
import com.kodilla.model.dataObject.ItemsOfBoard;
import com.kodilla.model.dataObject.StartValues;
import com.kodilla.model.constantly.BoardSize;
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

public class SearchQueenMoves {

    public static void movementOfQueen(Pawn pawn, Field[][] board, List<MoveOfPawn> allPawnsAndPositions) {
        board[pawn.getLastPositionX()][pawn.getLastPositionY()].setPawn(null);
        StartValues startValues = new StartValues(pawn, board).invoke();
        Node<OneStepMove> root = startValues.getRoot();
        ItemsOfBoard items = startValues.getElements();
        testIf(items, root);
        board[pawn.getLastPositionX()][pawn.getLastPositionY()].setPawn(pawn);

        List<Node<OneStepMove>> leafNodes = new ArrayList<>();
        ActionsOnTheTree.findLeafNode(root, leafNodes);

        List<MoveOfPawn> tempPawnAndPositions = new ArrayList<>();
        TreePath.createPath(pawn, leafNodes, tempPawnAndPositions);
        allPawnsAndPositions.addAll(tempPawnAndPositions);
    }

    private static void testIf(ItemsOfBoard items, Node<OneStepMove> lastNode) {
        if (items.getX() == 0 && items.getY() == 7) {
            chooseMoveOrKill(items, lastNode, addX_subY);
        } else if (items.getX() == 0 && items.getY() < 7) {
            chooseMoveOrKill(items, lastNode, addX_addY);
            chooseMoveOrKill(items, lastNode, addX_subY);
        } else if (items.getX() < 7 && items.getY() == 0) {
            chooseMoveOrKill(items, lastNode, addX_addY);
            chooseMoveOrKill(items, lastNode, subX_addY);
        } else if (items.getX() == 7 && items.getY() == 0) {
            chooseMoveOrKill(items, lastNode, subX_addY);
        } else if (items.getX() == 7 && items.getY() > 0) {
            chooseMoveOrKill(items, lastNode, subX_addY);
            chooseMoveOrKill(items, lastNode, subX_subY);
        } else if (items.getX() > 0 && items.getY() == 7) {
            chooseMoveOrKill(items, lastNode, addX_subY);
            chooseMoveOrKill(items, lastNode, subX_subY);
        } else if (items.getX() > 0 && items.getX() < 7 && items.getY() > 0 && items.getY() < 7) {
            chooseMoveOrKill(items, lastNode, addX_subY);
            chooseMoveOrKill(items, lastNode, addX_addY);
            chooseMoveOrKill(items, lastNode, subX_addY);
            chooseMoveOrKill(items, lastNode, subX_subY);
        }
    }

    private static void chooseMoveOrKill(ItemsOfBoard elements, Node<OneStepMove> lastNode, Coordinates coordinates) {
        if (isKill(elements, coordinates)) {
            List<Node<OneStepMove>> nodes = checkLineQueenKill(elements, lastNode, coordinates);
            testDiagonalKill(elements, nodes, coordinates);
        } else {
            simpleQueenMove(elements, lastNode, coordinates);
        }
    }

    private static boolean isKill(ItemsOfBoard items, Coordinates coor) {
        coor.setX_Y(items.getX(), items.getY());
        Field[][] board = items.getBoard();
        Pawn pawn = items.getPawn();
        while (coor.calculate().equals(BoardSize.IS_OK)) {
            if (Conditions.isKill(coor, board, pawn)) {
                return true;
            } else if (Conditions.isTwoPawnsNextToYourself(coor, board)) {
                return false;
            } else if (Conditions.isTheSameKindOfPawn(coor, board, pawn)) {
                return false;
            }
        }
        return false;
    }

    private static void simpleQueenMove(ItemsOfBoard items, Node<OneStepMove> lastNode, Coordinates coordinates) {
        coordinates.setX_Y(items.getX(), items.getY());
        while (!coordinates.calculate().equals(BoardSize.XY_OVERSIZE)) {
            if (!items.getBoard()[coordinates.getX()][coordinates.getY()].hasPawn()) {
                Position position = new Position(coordinates.getX(), coordinates.getY());
                OneStepMove oneStepMove = new OneStepMove(position, null, KindOfPosition.SIMPLE_MOVE);
                Node<OneStepMove> node = new Node<>(oneStepMove);
                lastNode.addChild(node);
            } else {
                return;
            }
        }
    }

    private static void testDiagonalKill(ItemsOfBoard elements, List<Node<OneStepMove>> nodes, Coordinates coordinates) {
        if (coordinates.equals(addX_subY) || coordinates.equals(subX_addY)) {
            findActiveFieldsAndDoDiagonal(elements, nodes, addX_addY);
            findActiveFieldsAndDoDiagonal(elements, nodes, subX_subY);
        } else if (coordinates.equals(addX_addY) || coordinates.equals(subX_subY)) {
            findActiveFieldsAndDoDiagonal(elements, nodes, subX_addY);
            findActiveFieldsAndDoDiagonal(elements, nodes, addX_subY);
        }
    }

    private static void findActiveFieldsAndDoDiagonal(ItemsOfBoard elements, List<Node<OneStepMove>> nodes, Coordinates coordinates) {
        List<Node<OneStepMove>> activeNodes = checkOneDirection(elements, nodes, coordinates);
        if (!activeNodes.isEmpty()) {
            testDiagonalKill(elements, activeNodes, coordinates);
        }
    }

    private static List<Node<OneStepMove>> checkOneDirection(ItemsOfBoard items, List<Node<OneStepMove>> nodes, Coordinates coordinates) {
        ItemsOfBoard itemsNewPosition = new ItemsOfBoard(items.getPawn(), items.getBoard(), items.getX(), items.getY());
        List<Node<OneStepMove>> activeNodes = new ArrayList<>();
        for (Node<OneStepMove> node : nodes) {
            Position position = node.getDate().getPosition();
            itemsNewPosition.setX(position.getX());
            itemsNewPosition.setY(position.getY());
            boolean kill = isKill(itemsNewPosition, coordinates);
            if (kill) {
                activeNodes.addAll(checkLineQueenKill(itemsNewPosition, node, coordinates));
            }
        }
        return activeNodes;
    }


    private static List<Node<OneStepMove>> checkLineQueenKill(ItemsOfBoard items, Node<OneStepMove> lastNode, Coordinates coor) {
        coor.setX_Y(items.getX(), items.getY());
        QueenKill queenKill = new QueenKill();
        while (true) {
            BoardSize calculate = coor.calculate();
            if (calculate.equals(BoardSize.XY_OVERSIZE)) {
                break;
            } else {
                List<Pawn> compactedPawns = ActionsOnTheTree.findCompactedPawns(lastNode);
                if (!queenKill.isKilledPawn() && Conditions.isRealKllAllCase(coor, items, compactedPawns)) {
                    queenKill.doWhenFirstKill(coor.getX(), coor.getY(), items.getBoard()[coor.getX()][coor.getY()], lastNode);
                } else if (queenKill.isKilledPawn() && !items.getBoard()[coor.getX()][coor.getY()].hasPawn()) {
                    queenKill.doWhenEmptyField(coor.getX(), coor.getY());
                } else if (!calculate.equals(BoardSize.NEXT_XY_OVERSIZE) && queenKill.isKilledPawn() && Conditions.isRealKllAllCase(coor, items, compactedPawns)) {
                    queenKill.doWhenNextKill(coor.getX(), coor.getY(), items.getBoard()[coor.getX()][coor.getY()]);
                } else if (!calculate.equals(BoardSize.NEXT_XY_OVERSIZE) && Conditions.isTwoPawnsNextToYourself(coor,items.getBoard())) {
                    return queenKill.getResult();
                } else if (Conditions.isTheSameKindOfPawn(coor,items.getBoard(),items.getPawn())) {
                    return queenKill.getResult();
                } else if (Conditions.pawnIsKilled(coor,items.getBoard(),compactedPawns)) {
                    return queenKill.getResult();
                }
            }
        }
        return queenKill.getResult();
    }
}
