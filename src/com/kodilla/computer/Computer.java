package com.kodilla.computer;

import com.kodilla.checkersExperience.Calculate;
import com.kodilla.checkersExperience.PawnAndPositions;
import com.kodilla.myEnum.PawnType;
import com.kodilla.testFindallMoves.PhysicalMovement;
import com.kodilla.testFindallMoves.Position;
import com.kodilla.tree.Node;
import com.kodilla.view.CheckersApp;
import com.kodilla.view.Field;
import com.kodilla.view.Pawn;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

public class Computer {

    private PawnType pawnType = PawnType.BLACK;

    public void test(Field[][] board){
        Field[][] copyingOfTheBoard = copyingOfTheBoard(board);
        List<PawnAndPositions> maxMovesAmongPawns = allMovesOfTheSelectedPawn(copyingOfTheBoard, pawnType);
        List<Node<MoveBoardAndPoint>> moveNodes = createNodes(maxMovesAmongPawns, board);
        List<Node<MoveBoardAndPoint>> nodes = doEachMoveNode(moveNodes);
        List<MoveBoardAndPoint> moveBoardAndPoints = calculatePoints(nodes);
        List<MoveBoardAndPoint> max = findMax(moveBoardAndPoints);
        doMaxMove(max, board);
    }

    private void doMaxMove(List<MoveBoardAndPoint> max, Field[][] board) {
        if (!max.isEmpty()) {
            MoveBoardAndPoint moveBoardAndPoint = max.get(0);
            PawnAndPositions pawnAndPositions = moveBoardAndPoint.getPawnAndPositions();
            List<Pawn> allCompactedPawns = pawnAndPositions.getAllCompactedPawns();
            Pawn pawnOwner = pawnAndPositions.getPawnOwner();
            pawnOwner.setLastPositionX(moveBoardAndPoint.getPosition().getX());
            pawnOwner.setLastPositionY(moveBoardAndPoint.getPosition().getY());
            Pawn pawn = findPawn(pawnOwner, board);
            PhysicalMovement.removeCompactedPawns(allCompactedPawns, board);
            Position lastPosition = pawnAndPositions.getLastPosition();
            PhysicalMovement.pawnMove(lastPosition.getX(), lastPosition.getY(), pawn, board);
        }
    }

    private Pawn findPawn(Pawn pawnOwner, Field[][] board) {
        Pawn pawn = null;
        for (Field[] fields : board) {
            for (Field field : fields) {
                if(field.hasPawn() && field.getPawn().equals(pawnOwner)){
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
        if(max.isPresent()){
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
            while (node != null){
                points += node.getDate().getPoint();
                if(node.isRoot()){
                    MoveBoardAndPoint moveBoardAndPoint = node.getDate();
                    moveBoardAndPoint.setPoint(points);
                    moveBoardAndPoints.add(moveBoardAndPoint);
                }
                node = node.getParent();
            }
        }
        return moveBoardAndPoints;
    }

    private List<PawnAndPositions> allMovesOfTheSelectedPawn(Field[][] board, PawnType pawnType) {
        List<PawnAndPositions> maxMovesAmongPawns;
        if(pawnType.equals(PawnType.BLACK)){
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
            List<PawnAndPositions> opponentMoves = allMovesOfTheSelectedPawn(moveNode.getDate().getBoard(), pawnType1);
            List<Node<MoveBoardAndPoint>> opponentMove = opponentMove(opponentMoves, moveNode);
            for (Node<MoveBoardAndPoint> opponentMoveNode : opponentMove) {
                PawnType pawnType2 = oneNodeDoMove(opponentMoveNode, pawnType1);
                List<PawnAndPositions> opponentMoves2 = allMovesOfTheSelectedPawn(opponentMoveNode.getDate().getBoard(), pawnType2);
                nodes.addAll(opponentMove(opponentMoves2, opponentMoveNode));
            }
        }
        return nodes;
    }

    private PawnType oneNodeDoMove(Node<MoveBoardAndPoint> moveNode, PawnType pawnType) {
        MoveBoardAndPoint moveNodeDate = moveNode.getDate();
        Field[][] board = moveNodeDate.getBoard();
        removePawns(moveNodeDate, board);
        Position lastPosition = moveNodeDate.getPawnAndPositions().getLastPosition();
        PhysicalMovement.pawnMove(lastPosition.getX(), lastPosition.getY(), moveNodeDate.getPawnAndPositions().getPawnOwner(), board);
        return pawnType.equals(PawnType.BLACK) ? PawnType.WHITE : PawnType.BLACK;
//        List<PawnAndPositions> opponentPawnAndPositions = allMovesOfTheSelectedPawn(board, pawnType);
//        return opponentMove(opponentPawnAndPositions, moveNode);
    }

    private List<Node<MoveBoardAndPoint>> opponentMove(List<PawnAndPositions> opponentPawnAndPositions, Node<MoveBoardAndPoint> moveNode) {
        List<PawnAndPositions> bestMove = chooseBestMove(opponentPawnAndPositions);
        List<Node<MoveBoardAndPoint>> opponentMoveNodes = createNodes(bestMove, moveNode.getDate().getBoard());
        moveNode.setChildren(opponentMoveNodes);
        return opponentMoveNodes;
//        doEachMoveNode(opponentMoveNodes);
    }

    private List<PawnAndPositions> chooseBestMove(List<PawnAndPositions> opponentPawnAndPositions) {
        List<PawnAndPositions> bestMove = new ArrayList<>();
        OptionalInt max = opponentPawnAndPositions.stream()
                .mapToInt(CalculatePoint::calculatePoint)
                .max();
        if (max.isPresent()){
            bestMove = opponentPawnAndPositions.stream()
                    .filter(pawnAndPositions -> CalculatePoint.calculatePoint(pawnAndPositions) == max.getAsInt())
                    .collect(Collectors.toList());
        }
        return bestMove;
    }

    private void removePawns(MoveBoardAndPoint moveNodeDate, Field[][] board) {
        List<Pawn> allCompactedPAwns = moveNodeDate.getPawnAndPositions().getAllCompactedPawns();
        if(!allCompactedPAwns.isEmpty()){
            PhysicalMovement.removeCompactedPawns(allCompactedPAwns, board);
        }
    }

    private List<Node<MoveBoardAndPoint>> createNodes(List<PawnAndPositions> maxMovesAmongPawns, Field[][] board) {
        List<Node<MoveBoardAndPoint>> moveBoardAndPointNodes = new ArrayList<>();
        for (PawnAndPositions pawnAndPositions : maxMovesAmongPawns) {
            int point = pawnAndPositions.getPawnOwner().getPawnType().equals(pawnType) ? CalculatePoint.calculatePoint(pawnAndPositions): CalculatePoint.calculatePoint(pawnAndPositions) * -1;
            Field[][] copyingOfTheBoard = copyingOfTheBoard(board);
            Position position = new Position(pawnAndPositions.getPawnOwner().getLastPositionX(), pawnAndPositions.getPawnOwner().getLastPositionY());
            try {
                pawnAndPositions.setPawnOwner((Pawn) pawnAndPositions.getPawnOwner().clone());
            } catch (CloneNotSupportedException e) {

            }
            MoveBoardAndPoint moveBoardAndPoint = new MoveBoardAndPoint(pawnAndPositions,copyingOfTheBoard,point, position);
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
}
