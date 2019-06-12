package com.kodilla.movementCalculation.dto;

import com.kodilla.movementCalculation.OneStepMove;
import com.kodilla.constantly.KindOfPosition;
import com.kodilla.oldElements.Position;
import com.kodilla.tree.Node;
import com.kodilla.oldElements.Field;
import com.kodilla.oldElements.Pawn;

public class StartValues {
    private Pawn pawn;
    private Field[][] board;
    private Node<OneStepMove> root;
    private ElementsOfBoardDto elements;

    public StartValues(Pawn pawn, Field[][] board) {
        this.pawn = pawn;
        this.board = board;
    }

    public Node<OneStepMove> getRoot() {
        return root;
    }

    public ElementsOfBoardDto getElements() {
        return elements;
    }

    public StartValues invoke() {
        Position startPosition = new Position(pawn.getLastPositionX(), pawn.getLastPositionY());
        OneStepMove oneStepMove = new OneStepMove(startPosition, null, KindOfPosition.START);
        root = new Node<>(oneStepMove);
        elements = new ElementsOfBoardDto(pawn, board, pawn.getLastPositionX(), pawn.getLastPositionY());
        return this;
    }
}
