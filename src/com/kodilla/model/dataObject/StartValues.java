package com.kodilla.model.dataObject;

import com.kodilla.model.movementCalculation.OneStepMove;
import com.kodilla.model.constantly.KindOfPosition;
import com.kodilla.model.elementsOfTheBoard.Position;
import com.kodilla.model.tree.Node;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;

public class StartValues {
    private Pawn pawn;
    private Field[][] board;
    private Node<OneStepMove> root;
    private ItemsOfBoard elements;

    public StartValues(Pawn pawn, Field[][] board) {
        this.pawn = pawn;
        this.board = board;
    }

    public Node<OneStepMove> getRoot() {
        return root;
    }

    public ItemsOfBoard getElements() {
        return elements;
    }

    public StartValues invoke() {
        Position startPosition = new Position(pawn.getLastPositionX(), pawn.getLastPositionY());
        OneStepMove oneStepMove = new OneStepMove(startPosition, null, KindOfPosition.START);
        root = new Node<>(oneStepMove);
        elements = new ItemsOfBoard(pawn, board, pawn.getLastPositionX(), pawn.getLastPositionY());
        return this;
    }
}
