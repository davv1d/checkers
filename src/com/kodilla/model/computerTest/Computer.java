package com.kodilla.model.computerTest;

import com.kodilla.model.boardBeaviour.ActionOnTheBoard;
import com.kodilla.model.boardBeaviour.Conditions;
import com.kodilla.model.boardBeaviour.CopyBoard;
import com.kodilla.model.dataObject.CheckedMovement;
import com.kodilla.model.dataObject.MoveBoardAndPoint;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;
import com.kodilla.model.elementsOfTheBoard.Position;
import com.kodilla.model.movementCalculation.calculateAll.Calculate;
import com.kodilla.model.movementCalculation.MoveOfPawn;
import com.kodilla.model.dataObject.MoveData;
import com.kodilla.model.tree.Node;
import com.kodilla.view.CheckersApp;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;

public class Computer {


    private boolean computerPawnsAreBlack = true;

    public CheckedMovement test(Field[][] board) {
        System.out.println("board before computerTest");
        printAllBoard(board);
        Field[][] copyingOfTheBoard = CopyBoard.invoke(board);
        List<MoveOfPawn> maxMovesAmongPawns = allMovesOfTheSelectedPawn(copyingOfTheBoard, computerPawnsAreBlack);
        List<Node<MoveBoardAndPoint>> moveNodes = createNodes(maxMovesAmongPawns, board);
        List<Node<MoveBoardAndPoint>> nodes = doEachMoveNode(moveNodes);
        List<MoveBoardAndPoint> moveBoardAndPoints = calculatePoints(nodes);
        List<MoveBoardAndPoint> max = findMax(moveBoardAndPoints);
        return doMaxMove(max, board);
    }

    private CheckedMovement doMaxMove(List<MoveBoardAndPoint> max, Field[][] board) {
        if (!max.isEmpty()) {
            Random random = new Random();
            int i = random.nextInt(max.size());
            MoveBoardAndPoint moveBoardAndPoint = max.get(i);
            MoveOfPawn moveOfPawn = moveBoardAndPoint.getMoveOfPawn();
            List<Pawn> allCompactedPawns = moveOfPawn.getAllCompactedPawns();

            List<Position> positions = allCompactedPawns.stream()
                    .map(pawn -> new Position(pawn.getLastPositionX(), pawn.getLastPositionY()))
                    .collect(Collectors.toList());

            Pawn pawnOwner = moveOfPawn.getPawnOwner();
            pawnOwner.setLastPositionX(moveBoardAndPoint.getPosition().getX());
            pawnOwner.setLastPositionY(moveBoardAndPoint.getPosition().getY());
            Pawn pawn = findPawnInOriginBoard(pawnOwner, board);
            System.out.println(moveOfPawn.getPositions());
            System.out.println("computerTest kill pawns " + allCompactedPawns);

            ActionOnTheBoard.removingKilledPawns(positions, board);

            Position lastPosition = moveOfPawn.getLastPosition();
            System.out.println("computerTest move " + lastPosition);

            boolean doQueen = Conditions.doQueen(pawn, lastPosition.getY(), true);
            MoveData moveData = new MoveData(pawn.getLastPositionX(), pawn.getLastPositionY(), lastPosition.getX(), lastPosition.getY());

            return new CheckedMovement(true, true, positions, doQueen, moveData, pawn);
        }
        return null;
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

    private List<MoveOfPawn> allMovesOfTheSelectedPawn(Field[][] board, boolean pawnIsBlack) {
        List<MoveOfPawn> maxMovesAmongPawns = new ArrayList<>();
        if (computerPawnsAreBlack == pawnIsBlack) {
            maxMovesAmongPawns = Calculate.getMaxMovesAmongBlack(board);
        } else {
            maxMovesAmongPawns = Calculate.getMaxMovesAmongWhite(board);
        }
        return maxMovesAmongPawns;
    }

    private List<Node<MoveBoardAndPoint>> doEachMoveNode(List<Node<MoveBoardAndPoint>> moveNodes) {
        List<Node<MoveBoardAndPoint>> nodes = new ArrayList<>();
        for (Node<MoveBoardAndPoint> moveNode : moveNodes) {
            boolean pawnIsBlack = oneNodeDoMove(moveNode, computerPawnsAreBlack);
            List<MoveOfPawn> opponentMoves = allMovesOfTheSelectedPawn(moveNode.getDate().getBoard(), pawnIsBlack);
            List<Node<MoveBoardAndPoint>> opponentMove = new ArrayList<>();
            if (opponentMoves.isEmpty()) {
                nodes.add(moveNode);
            } else {
                opponentMove = opponentMove(opponentMoves, moveNode);
            }
            for (Node<MoveBoardAndPoint> opponentMoveNode : opponentMove) {
                boolean pawnIsBlack2 = oneNodeDoMove(opponentMoveNode, pawnIsBlack);
                List<MoveOfPawn> opponentMoves2 = allMovesOfTheSelectedPawn(opponentMoveNode.getDate().getBoard(), pawnIsBlack2);
                List<Node<MoveBoardAndPoint>> computerMoves = opponentMove(opponentMoves2, opponentMoveNode);
                if (computerMoves.isEmpty()) {
                    nodes.add(opponentMoveNode);
                } else {
                    nodes.addAll(computerMoves);
                }
            }
        }
        return nodes;
    }

    private boolean oneNodeDoMove(Node<MoveBoardAndPoint> moveNode, boolean computerPawnsAreBlack) {
        MoveBoardAndPoint moveNodeDate = moveNode.getDate();
        Field[][] board = moveNodeDate.getBoard();
        removePawns(moveNodeDate, board);
        Position lastPosition = moveNodeDate.getMoveOfPawn().getLastPosition();
        Pawn pawnOwner = moveNodeDate.getMoveOfPawn().getPawnOwner();

        MoveData moveData = new MoveData(pawnOwner.getLastPositionX(), pawnOwner.getLastPositionY(), lastPosition.getX(), lastPosition.getY());
        ActionOnTheBoard.realMove(moveData, pawnOwner, board);
        return !computerPawnsAreBlack;
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

            List<Position> positions = allCompactedPAwns.stream()
                    .map(pawn -> new Position(pawn.getLastPositionX(), pawn.getLastPositionY()))
                    .collect(Collectors.toList());

            ActionOnTheBoard.removingKilledPawns(positions, board);
        }
    }

    private List<Node<MoveBoardAndPoint>> createNodes(List<MoveOfPawn> maxMovesAmongPawns, Field[][] board) {
        List<Node<MoveBoardAndPoint>> moveBoardAndPointNodes = new ArrayList<>();
        for (MoveOfPawn moveOfPawn : maxMovesAmongPawns) {
            int point = moveOfPawn.getPawnOwner().isBlack() == computerPawnsAreBlack ? CalculatePoint.calculatePoint(moveOfPawn) : CalculatePoint.calculatePoint(moveOfPawn) * -1;
            Field[][] copyingOfTheBoard = CopyBoard.invoke(board);
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

//
//    private Field[][] invoke(Field[][] board) {
//        Field[][] copyOfTheBoard = new Field[CheckersApp.WIDTH][CheckersApp.HEIGHT];
//        for (int x = 0; x < board.length; x++) {
//            for (int y = 0; y < board[x].length; y++) {
//                try {
//                    copyOfTheBoard[x][y] = (Field) board[x][y].clone();
//                } catch (CloneNotSupportedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return copyOfTheBoard;
//    }

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
