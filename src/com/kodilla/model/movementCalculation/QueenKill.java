package com.kodilla.model.movementCalculation;

import com.kodilla.model.constantly.KindOfPosition;
import com.kodilla.model.elementsOfTheBoard.Position;
import com.kodilla.model.tree.Node;
import com.kodilla.model.elementsOfTheBoard.Field;
import com.kodilla.model.elementsOfTheBoard.Pawn;

import java.util.ArrayList;
import java.util.List;

public class QueenKill {
    private Pawn killedPawn = null;
    private List<Node<OneStepMove>> emptyFields = new ArrayList<>();
    private List<Node<OneStepMove>> result = new ArrayList<>();
    private List<Node<OneStepMove>> positionsWithKilledPawns = new ArrayList<>();


    public boolean isKilledPawn() {
        return killedPawn != null;
    }

    public List<Node<OneStepMove>> getResult() {
        return result;
    }

    public void doWhenFirstKill(int x, int y, Field field, Node<OneStepMove> lastNode) {
        OneStepMove oneStepMove = new OneStepMove(new Position(x, y), field.getPawn(), KindOfPosition.KILL);
        Node<OneStepMove> killPosition = new Node<>(oneStepMove);
        killedPawn = field.getPawn();
        lastNode.addChild(killPosition);
        positionsWithKilledPawns.add(killPosition);

    }

    public void doWhenEmptyField(int x, int y) {
        OneStepMove oneStepMove = new OneStepMove(new Position(x, y), killedPawn, KindOfPosition.NONE);
        for (Node<OneStepMove> positionWithKilled : positionsWithKilledPawns) {
            Node<OneStepMove> emptyField = new Node<>(oneStepMove);
            emptyFields.add(emptyField);
            result.add(emptyField);
            positionWithKilled.addChild(emptyField);
        }
    }

    public void doWhenNextKill(int x, int y, Field field) {
        OneStepMove oneStepMove = new OneStepMove(new Position(x, y), field.getPawn(), KindOfPosition.KILL);
        killedPawn = field.getPawn();
        positionsWithKilledPawns.clear();
        addKillToEmptyField(oneStepMove);
        emptyFields.clear();
    }

    private void addKillToEmptyField(OneStepMove oneStepMove) {
        for (Node<OneStepMove> emptyField : emptyFields) {
            Node<OneStepMove> date = new Node<>(oneStepMove);
            positionsWithKilledPawns.add(date);
            emptyField.addChild(date);
        }
    }

}
