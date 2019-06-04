package com.kodilla.checkersExperience;

import com.kodilla.myEnum.KindOfPosition;
import com.kodilla.testFindallMoves.Position;
import com.kodilla.tree.Node;
import com.kodilla.view.Field;
import com.kodilla.view.Pawn;

public class StartValues {
    private Pawn pawn;
    private Field[][] board;
    private Node<PositionAndKilledPawn> root;
    private ElementsOfBoard elements;

    public StartValues(Pawn pawn, Field[][] board) {
        this.pawn = pawn;
        this.board = board;
    }

    public Node<PositionAndKilledPawn> getRoot() {
        return root;
    }

    public ElementsOfBoard getElements() {
        return elements;
    }

    public StartValues invoke() {
        Position startPosition = new Position(pawn.getLastPositionX(), pawn.getLastPositionY());
        PositionAndKilledPawn positionAndKilledPawn = new PositionAndKilledPawn(startPosition, null, KindOfPosition.START);
        root = new Node<>(positionAndKilledPawn);
        elements = new ElementsOfBoard(pawn, board, pawn.getLastPositionX(), pawn.getLastPositionY());
        return this;
    }
}
