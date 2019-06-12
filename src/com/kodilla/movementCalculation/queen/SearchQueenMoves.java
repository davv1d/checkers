package com.kodilla.movementCalculation.queen;

import com.kodilla.movementCalculation.Coordinates;
import com.kodilla.movementCalculation.MoveOfPawn;
import com.kodilla.movementCalculation.OneStepMove;
import com.kodilla.movementCalculation.QueenKill;
import com.kodilla.movementCalculation.dto.ElementsOfBoardDto;
import com.kodilla.movementCalculation.dto.StartValues;
import com.kodilla.constantly.BoardSize;
import com.kodilla.constantly.KindOfPosition;
import com.kodilla.oldElements.Position;
import com.kodilla.tree.TreePath;
import com.kodilla.tree.Node;
import com.kodilla.tree.LastNodesAndCompactedPawns;
import com.kodilla.oldElements.Field;
import com.kodilla.oldElements.Pawn;

import java.util.ArrayList;
import java.util.List;

import static com.kodilla.constantly.DefaultCoordinates.*;

public class SearchQueenMoves {

    public static void movementOfQueen(Pawn pawn, Field[][] board, List<MoveOfPawn> allPawnsAndPositions) {
        board[pawn.getLastPositionX()][pawn.getLastPositionY()].setPawn(null);
        StartValues startValues = new StartValues(pawn, board).invoke();
        Node<OneStepMove> root = startValues.getRoot();
        ElementsOfBoardDto elements = startValues.getElements();
        testIf(elements, root);
        board[pawn.getLastPositionX()][pawn.getLastPositionY()].setPawn(pawn);

        List<Node<OneStepMove>> leafNodes = new ArrayList<>();
        LastNodesAndCompactedPawns.findLeafNode(root, leafNodes);

        List<MoveOfPawn> tempPawnAndPositions = new ArrayList<>();
        TreePath.createPath(pawn, leafNodes, tempPawnAndPositions);
        allPawnsAndPositions.addAll(tempPawnAndPositions);
    }

    private static void testIf(ElementsOfBoardDto elements, Node<OneStepMove> lastNode) {
        if (elements.x == 0 && elements.y == 7) {
            chooseMoveOrKill(elements, lastNode, addX_subY);
        } else if (elements.x == 0 && elements.y < 7) {
            chooseMoveOrKill(elements, lastNode, addX_addY);
            chooseMoveOrKill(elements, lastNode, addX_subY);
        } else if (elements.x < 7 && elements.y == 0) {
            chooseMoveOrKill(elements, lastNode, addX_addY);
            chooseMoveOrKill(elements, lastNode, subX_addY);
        } else if (elements.x == 7 && elements.y == 0) {
            chooseMoveOrKill(elements, lastNode, subX_addY);
        } else if (elements.x == 7 && elements.y > 0) {
            chooseMoveOrKill(elements, lastNode, subX_addY);
            chooseMoveOrKill(elements, lastNode, subX_subY);
        } else if (elements.x > 0 && elements.y == 7) {
            chooseMoveOrKill(elements, lastNode, addX_subY);
            chooseMoveOrKill(elements, lastNode, subX_subY);
        } else if (elements.x > 0 && elements.x < 7 && elements.y > 0 && elements.y < 7) {
            chooseMoveOrKill(elements, lastNode, addX_subY);
            chooseMoveOrKill(elements, lastNode, addX_addY);
            chooseMoveOrKill(elements, lastNode, subX_addY);
            chooseMoveOrKill(elements, lastNode, subX_subY);
        }
    }

    private static void chooseMoveOrKill(ElementsOfBoardDto elements, Node<OneStepMove> lastNode, Coordinates coordinates) {
        if (isKill(elements, coordinates)) {
            List<Node<OneStepMove>> nodes = checkLineQueenKill(elements, lastNode, coordinates);
            testDiagonalKill(elements, nodes, coordinates);
        } else {
            simpleQueenMove(elements, lastNode, coordinates);
        }
    }

    private static boolean isKill(ElementsOfBoardDto elements, Coordinates coor) {
        coor.setX_Y(elements.x, elements.y);
        Field[][] board = elements.board;
        Pawn pawn = elements.pawn;
        while (coor.calculate().equals(BoardSize.IS_OK)) {
            if (board[coor.getX()][coor.getY()].hasPawn() &&
                    !board[coor.getX()][coor.getY()].getPawn().getPawnType().equals(pawn.getPawnType()) &&
                    !board[coor.getNextX()][coor.getNextY()].hasPawn()) {
                return true;
            } else if (board[coor.getX()][coor.getY()].hasPawn() && board[coor.getNextX()][coor.getNextY()].hasPawn()) {
                return false;
            } else if (board[coor.getX()][coor.getY()].hasPawn() && board[coor.getX()][coor.getY()].getPawn().getPawnType().equals(pawn.getPawnType())) {
                return false;
            }
        }
        return false;
    }

    private static void simpleQueenMove(ElementsOfBoardDto elements, Node<OneStepMove> lastNode, Coordinates coordinates) {
        coordinates.setX_Y(elements.x, elements.y);
        while (!coordinates.calculate().equals(BoardSize.XY_OVERSIZE)) {
            if (!elements.board[coordinates.getX()][coordinates.getY()].hasPawn()) {
                Position position = new Position(coordinates.getX(), coordinates.getY());
                OneStepMove oneStepMove = new OneStepMove(position, null, KindOfPosition.SIMPLE_MOVE);
                Node<OneStepMove> node = new Node<>(oneStepMove);
                lastNode.addChild(node);
            } else {
                return;
            }
        }
    }

    private static boolean isRealKllAllCase(Coordinates coor, ElementsOfBoardDto elements, List<Pawn> compactedPawns) {
        return elements.board[coor.getX()][coor.getY()].hasPawn() &&
                !elements.board[coor.getX()][coor.getY()].getPawn().getPawnType().equals(elements.pawn.getPawnType()) &&
                !elements.board[coor.getNextX()][coor.getNextY()].hasPawn() &&
                !compactedPawns.contains(elements.board[coor.getX()][coor.getY()].getPawn());
    }

    private static void testDiagonalKill(ElementsOfBoardDto elements, List<Node<OneStepMove>> nodes, Coordinates coordinates) {
        if (coordinates.equals(addX_subY) || coordinates.equals(subX_addY)) {
            findActiveFieldsAndDoDiagonal(elements, nodes, addX_addY);
            findActiveFieldsAndDoDiagonal(elements, nodes, subX_subY);
        } else if (coordinates.equals(addX_addY) || coordinates.equals(subX_subY)) {
            findActiveFieldsAndDoDiagonal(elements, nodes, subX_addY);
            findActiveFieldsAndDoDiagonal(elements, nodes, addX_subY);
        }
    }

    private static void findActiveFieldsAndDoDiagonal(ElementsOfBoardDto elements, List<Node<OneStepMove>> nodes, Coordinates coordinates) {
        List<Node<OneStepMove>> activeNodes = checkOneDirection(elements, nodes, coordinates);
        if (!activeNodes.isEmpty()) {
            testDiagonalKill(elements, activeNodes, coordinates);
        }
    }

    private static List<Node<OneStepMove>> checkOneDirection(ElementsOfBoardDto elements, List<Node<OneStepMove>> nodes, Coordinates coordinates) {
        ElementsOfBoardDto elementsNewPosition = new ElementsOfBoardDto(elements.pawn, elements.board, elements.x, elements.y);
        List<Node<OneStepMove>> activeNodes = new ArrayList<>();
        for (Node<OneStepMove> node : nodes) {
            Position position = node.getDate().getPosition();
            elementsNewPosition.x = position.getX();
            elementsNewPosition.y = position.getY();
            boolean kill = isKill(elementsNewPosition, coordinates);
            if (kill) {
                activeNodes.addAll(checkLineQueenKill(elementsNewPosition, node, coordinates));
            }
        }
        return activeNodes;
    }


    private static List<Node<OneStepMove>> checkLineQueenKill(ElementsOfBoardDto elements, Node<OneStepMove> lastNode, Coordinates coor) {
        coor.setX_Y(elements.x, elements.y);
        QueenKill queenKill = new QueenKill();
        while (true) {
            BoardSize calculate = coor.calculate();
            if (calculate.equals(BoardSize.XY_OVERSIZE)) {
                break;
            } else {
                List<Pawn> compactedPawns = LastNodesAndCompactedPawns.findCompactedPawns(lastNode);
                if (!queenKill.isKilledPawn() && isRealKllAllCase(coor, elements, compactedPawns)) {
                    queenKill.doWhenFirstKill(coor.getX(), coor.getY(), elements.board[coor.getX()][coor.getY()], lastNode);
                } else if (queenKill.isKilledPawn() && !elements.board[coor.getX()][coor.getY()].hasPawn()) {
                    queenKill.doWhenEmptyField(coor.getX(), coor.getY());
                } else if (!calculate.equals(BoardSize.NEXT_XY_OVERSIZE) && queenKill.isKilledPawn() && isRealKllAllCase(coor, elements, compactedPawns)) {
                    queenKill.doWhenNextKill(coor.getX(), coor.getY(), elements.board[coor.getX()][coor.getY()]);
                } else if (!calculate.equals(BoardSize.NEXT_XY_OVERSIZE) && elements.board[coor.getX()][coor.getY()].hasPawn() && elements.board[coor.getNextX()][coor.getNextY()].hasPawn()) {
                    return queenKill.getResult();
                } else if (elements.isTheSamePawnType(coor.getX(), coor.getY())) {
                    return queenKill.getResult();
                } else if (elements.board[coor.getX()][coor.getY()].hasPawn() && compactedPawns.contains(elements.board[coor.getX()][coor.getY()].getPawn())) {
                    return queenKill.getResult();
                }
            }
        }
        return queenKill.getResult();
    }
}
