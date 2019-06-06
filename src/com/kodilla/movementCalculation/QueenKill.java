package com.kodilla.movementCalculation;

import com.kodilla.constantly.KindOfPosition;
import com.kodilla.oldElements.Position;
import com.kodilla.tree.Node;
import com.kodilla.oldElements.Field;
import com.kodilla.oldElements.Pawn;

import java.util.ArrayList;
import java.util.List;

public class QueenKill {
    private Pawn killedPawn = null;
    private List<Node<PositionAndKilledPawn>> emptyFields = new ArrayList<>();
    private List<Node<PositionAndKilledPawn>> result = new ArrayList<>();
    private List<Node<PositionAndKilledPawn>> positionsWithKilledPawns = new ArrayList<>();


    public boolean isKilledPawn() {
        return killedPawn != null;
    }

    public List<Node<PositionAndKilledPawn>> getResult() {
        return result;
    }

    public void doWhenFirstKill(int x, int y, Field field, Node<PositionAndKilledPawn> lastNode) {
        PositionAndKilledPawn positionAndKilledPawn = new PositionAndKilledPawn(new Position(x, y), field.getPawn(), KindOfPosition.KILL);
        Node<PositionAndKilledPawn> killPosition = new Node<>(positionAndKilledPawn);
        killedPawn = field.getPawn();
        lastNode.addChild(killPosition);
        positionsWithKilledPawns.add(killPosition);

    }

    public void doWhenEmptyField(int x, int y) {
        PositionAndKilledPawn positionAndKilledPawn = new PositionAndKilledPawn(new Position(x, y), killedPawn, KindOfPosition.NONE);
        for (Node<PositionAndKilledPawn> positionWithKilled : positionsWithKilledPawns) {
            Node<PositionAndKilledPawn> emptyField = new Node<>(positionAndKilledPawn);
            emptyFields.add(emptyField);
            result.add(emptyField);
            positionWithKilled.addChild(emptyField);
        }
    }

    public void doWhenNextKill(int x, int y, Field field) {
        PositionAndKilledPawn positionAndKilledPawn = new PositionAndKilledPawn(new Position(x, y), field.getPawn(), KindOfPosition.KILL);
        killedPawn = field.getPawn();
        positionsWithKilledPawns.clear();
        addKillToEmptyField(positionAndKilledPawn);
        emptyFields.clear();
    }

    private void addKillToEmptyField(PositionAndKilledPawn positionAndKilledPawn) {
        for (Node<PositionAndKilledPawn> emptyField : emptyFields) {
            Node<PositionAndKilledPawn> date = new Node<>(positionAndKilledPawn);
            positionsWithKilledPawns.add(date);
            emptyField.addChild(date);
        }
    }

}
