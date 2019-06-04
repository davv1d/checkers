package com.kodilla.checkersExperience;

import com.kodilla.myEnum.BoardSize;
import com.kodilla.myEnum.KindOfPosition;
import com.kodilla.testFindallMoves.Position;
import com.kodilla.tree.Node;
import com.kodilla.view.Field;
import com.kodilla.view.Pawn;

import java.util.ArrayList;
import java.util.List;

import static com.kodilla.checkersExperience.DefaultCoordinates.*;

public class SearchQueenMoves {

    public static void movementOfQueen(Pawn pawn, Field[][] board, List<PawnAndPositions> allPawnsAndPositions) {
        board[pawn.getLastPositionX()][pawn.getLastPositionY()].setPawn(null);
        StartValues startValues = new StartValues(pawn, board).invoke();
        Node<PositionAndKilledPawn> root = startValues.getRoot();
        ElementsOfBoard elements = startValues.getElements();
        testIf(elements, root);
        board[pawn.getLastPositionX()][pawn.getLastPositionY()].setPawn(pawn);

        List<Node<PositionAndKilledPawn>> leafNodes = new ArrayList<>();
        LastNodesAndCompactedPAwns.findLeafNode(root, leafNodes);

        List<PawnAndPositions> tempPawnAndPositions = new ArrayList<>();
        CreatePathFromTheTree.createPath(pawn, leafNodes, tempPawnAndPositions);
        allPawnsAndPositions.addAll(tempPawnAndPositions);
    }

    private static void testIf(ElementsOfBoard elements, Node<PositionAndKilledPawn> lastNode) {
        if (elements.x == 0 && elements.y == 7) {
            chooseMoveOrKill(elements, lastNode, addX_subY);
        } else if (elements.x == 0 && elements.y < 7) {
            chooseMoveOrKill(elements, lastNode, addX_addY);
            chooseMoveOrKill(elements, lastNode, addX_subY);
        } else if (elements.x < 7 && elements.y == 0) {
            chooseMoveOrKill(elements, lastNode, addX_addY);
            chooseMoveOrKill(elements, lastNode, addX_subY);
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

    private static void chooseMoveOrKill(ElementsOfBoard elements, Node<PositionAndKilledPawn> lastNode, Coordinates coordinates) {
        if (isKill(elements, coordinates)) {
            List<Node<PositionAndKilledPawn>> nodes = checkLineQueenKill(elements, lastNode, coordinates);
            testDiagonalKill(elements, nodes, coordinates);
        } else {
            simpleQueenMove(elements, lastNode, coordinates);
        }
    }

    private static boolean isKill(ElementsOfBoard elements, Coordinates coor) {
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

    private static void simpleQueenMove(ElementsOfBoard elements, Node<PositionAndKilledPawn> lastNode, Coordinates coordinates) {
        coordinates.setX_Y(elements.x, elements.y);
        while (!coordinates.calculate().equals(BoardSize.XY_OVERSIZE)) {
            if (!elements.board[coordinates.getX()][coordinates.getY()].hasPawn()) {
                Position position = new Position(coordinates.getX(), coordinates.getY());
                PositionAndKilledPawn positionAndKilledPawn = new PositionAndKilledPawn(position, null, KindOfPosition.SIMPLE_MOVE);
                Node<PositionAndKilledPawn> node = new Node<>(positionAndKilledPawn);
                lastNode.addChild(node);
            } else {
                return;
            }
        }
    }

    private static boolean isRealKllAllCase(Coordinates coor, ElementsOfBoard elements, List<Pawn> compactedPawns) {
        return elements.board[coor.getX()][coor.getY()].hasPawn() &&
                !elements.board[coor.getX()][coor.getY()].getPawn().getPawnType().equals(elements.pawn.getPawnType()) &&
                !elements.board[coor.getNextX()][coor.getNextY()].hasPawn() &&
                !compactedPawns.contains(elements.board[coor.getX()][coor.getY()].getPawn());
    }

    private static void testDiagonalKill(ElementsOfBoard elements, List<Node<PositionAndKilledPawn>> nodes, Coordinates coordinates) {
        if (coordinates.equals(addX_subY) || coordinates.equals(subX_addY)) {
            findActiveFieldsAndDoDiagonal(elements, nodes, addX_addY);
            findActiveFieldsAndDoDiagonal(elements, nodes, subX_subY);
        } else if (coordinates.equals(addX_addY) || coordinates.equals(subX_subY)) {
            findActiveFieldsAndDoDiagonal(elements, nodes, subX_addY);
            findActiveFieldsAndDoDiagonal(elements, nodes, addX_subY);
        }
    }

    private static void findActiveFieldsAndDoDiagonal(ElementsOfBoard elements, List<Node<PositionAndKilledPawn>> nodes, Coordinates coordinates) {
        List<Node<PositionAndKilledPawn>> activeNodes = checkOneDirection(elements, nodes, coordinates);
        if (!activeNodes.isEmpty()) {
            testDiagonalKill(elements, activeNodes, coordinates);
        }
    }

    private static List<Node<PositionAndKilledPawn>> checkOneDirection(ElementsOfBoard elements, List<Node<PositionAndKilledPawn>> nodes, Coordinates coordinates) {
        ElementsOfBoard elementsNewPosition = new ElementsOfBoard(elements.pawn, elements.board, elements.x, elements.y);
        List<Node<PositionAndKilledPawn>> activeNodes = new ArrayList<>();
        for (Node<PositionAndKilledPawn> node : nodes) {
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


    private static List<Node<PositionAndKilledPawn>> checkLineQueenKill(ElementsOfBoard elements, Node<PositionAndKilledPawn> lastNode, Coordinates coor) {
        coor.setX_Y(elements.x, elements.y);
        QueenKill queenKill = new QueenKill();
        while (true) {
            BoardSize calculate = coor.calculate();
            if (calculate.equals(BoardSize.XY_OVERSIZE)) {
                break;
            } else {
                List<Pawn> compactedPawns = LastNodesAndCompactedPAwns.findCompactedPawns(lastNode);
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
