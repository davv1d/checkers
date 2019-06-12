package com.kodilla.computerTest;

import com.kodilla.movementCalculation.calculateAll.Calculate;
import com.kodilla.movementCalculation.MoveOfPawn;
import com.kodilla.constantly.PawnType;
import com.kodilla.oldElements.PhysicalMovement;
import com.kodilla.oldElements.Position;
import com.kodilla.tree.Node;
import com.kodilla.view.CheckersApp;
import com.kodilla.oldElements.Field;
import com.kodilla.oldElements.Pawn;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;

public class Computer {

    private PawnType pawnType = PawnType.BLACK;

    public void test(Field[][] board) {
        System.out.println("board before computerTest");
        printAllBoard(board);
        Field[][] copyingOfTheBoard = copyingOfTheBoard(board);
        List<MoveOfPawn> maxMovesAmongPawns = allMovesOfTheSelectedPawn(copyingOfTheBoard, pawnType);
        List<Node<MoveBoardAndPoint>> moveNodes = createNodes(maxMovesAmongPawns, board);
        List<Node<MoveBoardAndPoint>> nodes = doEachMoveNode(moveNodes);
        List<MoveBoardAndPoint> moveBoardAndPoints = calculatePoints(nodes);
        List<MoveBoardAndPoint> max = findMax(moveBoardAndPoints);
        doMaxMove(max, board);
    }

    private void doMaxMove(List<MoveBoardAndPoint> max, Field[][] board) {
        if (!max.isEmpty()) {
            Random random = new Random();
            int i = random.nextInt(max.size());
            MoveBoardAndPoint moveBoardAndPoint = max.get(i);
            MoveOfPawn moveOfPawn = moveBoardAndPoint.getMoveOfPawn();
            List<Pawn> allCompactedPawns = moveOfPawn.getAllCompactedPawns();
            Pawn pawnOwner = moveOfPawn.getPawnOwner();
            pawnOwner.setLastPositionX(moveBoardAndPoint.getPosition().getX());
            pawnOwner.setLastPositionY(moveBoardAndPoint.getPosition().getY());
            Pawn pawn = findPawnInOriginBoard(pawnOwner, board);
            System.out.println(moveOfPawn.getPositions());
            System.out.println("computerTest kill pawns " + allCompactedPawns);
            PhysicalMovement.removeCompactedPawns(allCompactedPawns, board);
            Position lastPosition = moveOfPawn.getLastPosition();
            System.out.println("computerTest move " + lastPosition);
            PhysicalMovement.doQueen(lastPosition.getY(), pawn);
            PhysicalMovement.pawnMove(lastPosition.getX(), lastPosition.getY(), pawn, board);
        }
    }

    private Pawn findPawnInOriginBoard(Pawn pawnOwner, Field[][] board) {
        Pawn pawn = null;
        for (Field[] fields : board) {
            for (Field field : fields) {
                if (field.hasPawn() && field.getPawn().equals(pawnOwner)) {
                    return field.getPawn();
                }
            }
        }
        return pawn;
    }

    private List<MoveBoardAndPoint> findMax(List<MoveBoardAndPoint> moveBoardAndPoints) {
        List<MoveBoardAndPoint> maxList = new ArrayList<>();
        OptionalInt max = moveBoardAndPoints.stream()
                .mapToInt(MoveBoardAndPoint::getPoint)
                .max();
        if (max.isPresent()) {
            maxList = moveBoardAndPoints.stream()
                    .filter(moveBoardAndPoint -> moveBoardAndPoint.getPoint() == max.getAsInt())
                    .collect(Collectors.toList());
        }
        return maxList;
    }

    private List<MoveBoardAndPoint> calculatePoints(List<Node<MoveBoardAndPoint>> nodes) {
        List<MoveBoardAndPoint> moveBoardAndPoints = new ArrayList<>();
        for (Node<MoveBoardAndPoint> node : nodes) {
            int points = 0;
            while (node != null) {
                points += node.getDate().getPoint();
                if (node.isRoot()) {
                    MoveBoardAndPoint moveBoardAndPoint = node.getDate();
                    moveBoardAndPoint.setPoint(points);
                    moveBoardAndPoints.add(moveBoardAndPoint);
                }
                node = node.getParent();
            }
        }
        return moveBoardAndPoints;
    }

    private List<MoveOfPawn> allMovesOfTheSelectedPawn(Field[][] board, PawnType pawnType) {
        List<MoveOfPawn> maxMovesAmongPawns;
        if (pawnType.equals(PawnType.BLACK)) {
            maxMovesAmongPawns = Calculate.getMaxMovesAmongBlack(board);
        } else {
            maxMovesAmongPawns = Calculate.getMaxMovesAmongWhite(board);
        }
        return maxMovesAmongPawns;
    }

    private List<Node<MoveBoardAndPoint>> doEachMoveNode(List<Node<MoveBoardAndPoint>> moveNodes) {
        List<Node<MoveBoardAndPoint>> nodes = new ArrayList<>();
        for (Node<MoveBoardAndPoint> moveNode : moveNodes) {
            PawnType pawnType1 = oneNodeDoMove(moveNode, pawnType);
            List<MoveOfPawn> opponentMoves = allMovesOfTheSelectedPawn(moveNode.getDate().getBoard(), pawnType1);
            List<Node<MoveBoardAndPoint>> opponentMove = new ArrayList<>();
            if(opponentMoves.isEmpty()){
                nodes.add(moveNode);
            } else {
                opponentMove = opponentMove(opponentMoves, moveNode);
            }
            for (Node<MoveBoardAndPoint> opponentMoveNode : opponentMove) {
                PawnType pawnType2 = oneNodeDoMove(opponentMoveNode, pawnType1);
                List<MoveOfPawn> opponentMoves2 = allMovesOfTheSelectedPawn(opponentMoveNode.getDate().getBoard(), pawnType2);
                List<Node<MoveBoardAndPoint>> computerMoves = opponentMove(opponentMoves2, opponentMoveNode);
                if(computerMoves.isEmpty()) {
                    nodes.add(opponentMoveNode);
                } else {
                    nodes.addAll(computerMoves);
                }
            }
        }
        return nodes;
    }

    private PawnType oneNodeDoMove(Node<MoveBoardAndPoint> moveNode, PawnType pawnType) {
        MoveBoardAndPoint moveNodeDate = moveNode.getDate();
        Field[][] board = moveNodeDate.getBoard();
        removePawns(moveNodeDate, board);
        Position lastPosition = moveNodeDate.getMoveOfPawn().getLastPosition();
        PhysicalMovement.pawnMove(lastPosition.getX(), lastPosition.getY(), moveNodeDate.getMoveOfPawn().getPawnOwner(), board);
        return pawnType.equals(PawnType.BLACK) ? PawnType.WHITE : PawnType.BLACK;
    }

    private List<Node<MoveBoardAndPoint>> opponentMove(List<MoveOfPawn> opponentPawnAndPositions, Node<MoveBoardAndPoint> moveNode) {
        List<MoveOfPawn> bestMove = chooseBestMove(opponentPawnAndPositions);
        List<Node<MoveBoardAndPoint>> opponentMoveNodes = createNodes(bestMove, moveNode.getDate().getBoard());
        moveNode.setChildren(opponentMoveNodes);
        return opponentMoveNodes;
    }

    private List<MoveOfPawn> chooseBestMove(List<MoveOfPawn> opponentPawnAndPositions) {
        List<MoveOfPawn> bestMove = new ArrayList<>();
        OptionalInt max = opponentPawnAndPositions.stream()
                .mapToInt(CalculatePoint::calculatePoint)
                .max();
        if (max.isPresent()) {
            bestMove = opponentPawnAndPositions.stream()
                    .filter(pawnAndPositions -> CalculatePoint.calculatePoint(pawnAndPositions) == max.getAsInt())
                    .collect(Collectors.toList());
        }
        return bestMove;
    }

    private void removePawns(MoveBoardAndPoint moveNodeDate, Field[][] board) {
        List<Pawn> allCompactedPAwns = moveNodeDate.getMoveOfPawn().getAllCompactedPawns();
        if (!allCompactedPAwns.isEmpty()) {
            PhysicalMovement.removeCompactedPawns(allCompactedPAwns, board);
        }
    }

    private List<Node<MoveBoardAndPoint>> createNodes(List<MoveOfPawn> maxMovesAmongPawns, Field[][] board) {
        List<Node<MoveBoardAndPoint>> moveBoardAndPointNodes = new ArrayList<>();
        for (MoveOfPawn moveOfPawn : maxMovesAmongPawns) {
            int point = moveOfPawn.getPawnOwner().getPawnType().equals(pawnType) ? CalculatePoint.calculatePoint(moveOfPawn) : CalculatePoint.calculatePoint(moveOfPawn) * -1;
            Field[][] copyingOfTheBoard = copyingOfTheBoard(board);
            Position position = new Position(moveOfPawn.getPawnOwner().getLastPositionX(), moveOfPawn.getPawnOwner().getLastPositionY());
            MoveOfPawn copyMoveOfPawn = null;
            try {
                copyMoveOfPawn = new MoveOfPawn((Pawn) moveOfPawn.getPawnOwner().clone(), moveOfPawn.getPositions());
            } catch (CloneNotSupportedException e) {
                System.out.println(e.getMessage());
            }
            MoveBoardAndPoint moveBoardAndPoint = new MoveBoardAndPoint(copyMoveOfPawn, copyingOfTheBoard, point, position);
            Node<MoveBoardAndPoint> moveNodes = new Node<>(moveBoardAndPoint);
            moveBoardAndPointNodes.add(moveNodes);
        }
        return moveBoardAndPointNodes;
    }


    private Field[][] copyingOfTheBoard(Field[][] board) {
        Field[][] copyOfTheBoard = new Field[CheckersApp.WIDTH][CheckersApp.HEIGHT];
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                try {
                    copyOfTheBoard[x][y] = (Field) board[x][y].clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }
        return copyOfTheBoard;
    }

    public void printAllBoard(Field[][] board) {
        System.out.println("______________________________________________________________________________________");
        for (int y = 0; y < CheckersApp.HEIGHT; y++) {
            System.out.print(y + " |");
            for (int x = 0; x < CheckersApp.WIDTH; x++) {
                if (board[x][y].hasPawn()) {
                    System.out.print(board[x][y].getPawn() + " | ");
                }
            }
            System.out.println("\n");
        }
        System.out.println("______________________________________________________________________________________");
    }
}
