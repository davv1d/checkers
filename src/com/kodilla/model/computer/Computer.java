package com.kodilla.model.computer;

import com.kodilla.model.boardBeaviour.ActionsOnThePawns;
import com.kodilla.model.boardBeaviour.Conditions;
import com.kodilla.model.boardBeaviour.ActionsOnTheBoard;
import com.kodilla.model.dataObject.ModelData;
import com.kodilla.model.dataObject.ComputerData;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;
import com.kodilla.model.elementsOfTheBoard.Position;
import com.kodilla.model.movementCalculation.calculateAll.Calculate;
import com.kodilla.model.movementCalculation.MoveOfPawn;
import com.kodilla.model.dataObject.MoveData;
import com.kodilla.model.tree.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;

public class Computer {
    private boolean computerPawnsAreBlack = true;

    public ModelData computerMove(Field[][] board) {
        Field[][] copyingOfTheBoard = ActionsOnTheBoard.invoke(board);
        List<MoveOfPawn> maxMovesAmongPawns = allMovesOfTheSelectedPawn(copyingOfTheBoard, computerPawnsAreBlack);
        List<Node<ComputerData>> moveNodes = createNodes(maxMovesAmongPawns, board);
        List<Node<ComputerData>> nodes = doEachMoveNode(moveNodes);
        List<ComputerData> computerData = calculatePoints(nodes);
        List<ComputerData> max = findMax(computerData);
        return doMaxMove(max, board);
    }

    private List<MoveOfPawn> allMovesOfTheSelectedPawn(Field[][] board, boolean pawnIsBlack) {
        List<MoveOfPawn> maxMovesAmongPawns;
        if (computerPawnsAreBlack == pawnIsBlack) {
            maxMovesAmongPawns = Calculate.getMaxMovesAmongBlack(board);
        } else {
            maxMovesAmongPawns = Calculate.getMaxMovesAmongWhite(board);
        }
        return maxMovesAmongPawns;
    }

    private List<Node<ComputerData>> createNodes(List<MoveOfPawn> maxMovesAmongPawns, Field[][] board) {
        List<Node<ComputerData>> moveBoardAndPointNodes = new ArrayList<>();
        for (MoveOfPawn moveOfPawn : maxMovesAmongPawns) {
            int point = moveOfPawn.getPawnOwner().isBlack() == computerPawnsAreBlack ? CalculatePoint.calculatePoint(moveOfPawn) : CalculatePoint.calculatePoint(moveOfPawn) * -1;
            Field[][] copyingOfTheBoard = ActionsOnTheBoard.invoke(board);
            Position position = new Position(moveOfPawn.getPawnOwner().getLastPositionX(), moveOfPawn.getPawnOwner().getLastPositionY());
            MoveOfPawn copyMoveOfPawn = null;
            try {
                copyMoveOfPawn = new MoveOfPawn((Pawn) moveOfPawn.getPawnOwner().clone(), moveOfPawn.getPositions());
            } catch (CloneNotSupportedException e) {
                System.out.println(e.getMessage());
            }
            ComputerData computerData = new ComputerData(copyMoveOfPawn, copyingOfTheBoard, point, position);
            Node<ComputerData> moveNodes = new Node<>(computerData);
            moveBoardAndPointNodes.add(moveNodes);
        }
        return moveBoardAndPointNodes;
    }

    private ModelData doMaxMove(List<ComputerData> max, Field[][] board) {
        if (!max.isEmpty()) {
            Random random = new Random();
            int i = random.nextInt(max.size());
            ComputerData computerData = max.get(i);
            MoveOfPawn moveOfPawn = computerData.getMoveOfPawn();
            List<Position> allCompactedPawns = moveOfPawn.getAllCompactedPawns();
            Pawn pawnOwner = moveOfPawn.getPawnOwner();
            pawnOwner.setLastPositionX(computerData.getPosition().getX());
            pawnOwner.setLastPositionY(computerData.getPosition().getY());
            Pawn pawn = ActionsOnTheBoard.findPawn(board,pawnOwner.getLastPositionX(), pawnOwner.getLastPositionY());
            ActionsOnThePawns.removingKilledPawns(allCompactedPawns, board);
            Position lastPosition = moveOfPawn.getLastPosition();
            boolean doQueen = Conditions.doQueen(pawn, lastPosition.getY(), true);
            MoveData moveData = new MoveData(pawn.getLastPositionX(), pawn.getLastPositionY(), lastPosition.getX(), lastPosition.getY());
            return new ModelData(true, true, allCompactedPawns, doQueen, moveData, pawn);
        }
        return new ModelData();
    }

    private List<ComputerData> findMax(List<ComputerData> computerDataList) {
        List<ComputerData> maxList = new ArrayList<>();
        OptionalInt max = computerDataList.stream()
                .mapToInt(ComputerData::getPoint)
                .max();
        if (max.isPresent()) {
            maxList = computerDataList.stream()
                    .filter(compData -> compData.getPoint() == max.getAsInt())
                    .collect(Collectors.toList());
        }
        return maxList;
    }

    private List<ComputerData> calculatePoints(List<Node<ComputerData>> nodes) {
        List<ComputerData> computerDataList = new ArrayList<>();
        for (Node<ComputerData> node : nodes) {
            int points = 0;
            while (node != null) {
                points += node.getDate().getPoint();
                if (node.isRoot()) {
                    ComputerData compData = node.getDate();
                    compData.setPoint(points);
                    computerDataList.add(compData);
                }
                node = node.getParent();
            }
        }
        return computerDataList;
    }

    private List<Node<ComputerData>> doEachMoveNode(List<Node<ComputerData>> moveNodes) {
        List<Node<ComputerData>> nodes = new ArrayList<>();
        for (Node<ComputerData> moveNode : moveNodes) {
            boolean opponentPawnIsBlack = oneNodeDoMove(moveNode, computerPawnsAreBlack);
            List<MoveOfPawn> opponentMoves = allMovesOfTheSelectedPawn(moveNode.getDate().getBoard(), opponentPawnIsBlack);
            List<Node<ComputerData>> opponentMove = new ArrayList<>();
            if (opponentMoves.isEmpty()) {
                nodes.add(moveNode);
            } else {
                opponentMove = opponentMove(opponentMoves, moveNode);
            }
            for (Node<ComputerData> opponentMoveNode : opponentMove) {
                boolean compPawnIsBlack = oneNodeDoMove(opponentMoveNode, opponentPawnIsBlack);
                List<MoveOfPawn> playerMoves = allMovesOfTheSelectedPawn(opponentMoveNode.getDate().getBoard(), compPawnIsBlack);
                List<Node<ComputerData>> computerMoves = opponentMove(playerMoves, opponentMoveNode);
                if (computerMoves.isEmpty()) {
                    nodes.add(opponentMoveNode);
                } else {
                    nodes.addAll(computerMoves);
                }
            }
        }
        return nodes;
    }

    private boolean oneNodeDoMove(Node<ComputerData> moveNode, boolean computerPawnsAreBlack) {
        ComputerData moveNodeDate = moveNode.getDate();
        Field[][] board = moveNodeDate.getBoard();
        ActionsOnThePawns.removingKilledPawns(moveNodeDate.getMoveOfPawn().getAllCompactedPawns(), board);
        Position lastPosition = moveNodeDate.getMoveOfPawn().getLastPosition();
        Pawn pawnOwner = moveNodeDate.getMoveOfPawn().getPawnOwner();
        MoveData moveData = new MoveData(pawnOwner.getLastPositionX(), pawnOwner.getLastPositionY(), lastPosition.getX(), lastPosition.getY());
        ActionsOnThePawns.realMove(moveData, pawnOwner, board);
        return !computerPawnsAreBlack;
    }

    private List<Node<ComputerData>> opponentMove(List<MoveOfPawn> opponentPawnAndPositions, Node<ComputerData> moveNode) {
        List<MoveOfPawn> bestMove = chooseBestMove(opponentPawnAndPositions);
        List<Node<ComputerData>> opponentMoveNodes = createNodes(bestMove, moveNode.getDate().getBoard());
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
}
