package com.kodilla.model.tree;

import com.kodilla.model.movementCalculation.OneStepMove;
import com.kodilla.model.elementsOfTheBoard.Pawn;

import java.util.ArrayList;
import java.util.List;

public class ActionsOnTheTree {

    public static List<Pawn> findCompactedPawns(Node<OneStepMove> lastNode) {
        List<Pawn> compactedPawns = new ArrayList<>();
        if (lastNode != null) {
            Node<OneStepMove> node = lastNode;
            Pawn killedPawn = null;
            while (node != null) {
                if (node.getDate().getCompactedPawn() != null && !node.getDate().getCompactedPawn().equals(killedPawn)) {
                    killedPawn = node.getDate().getCompactedPawn();
                    compactedPawns.add(killedPawn);
                }
                node = node.getParent();
            }
        }
        return compactedPawns;
    }

    public static void findLeafNode(Node<OneStepMove> node, List<Node<OneStepMove>> leafNodes) {
        if(node.isLeafNode() && node.getParent() == null){
            return;
        } else if (node.isLeafNode()) {
            leafNodes.add(node);
        } else {
            for (int i = 0; i < node.getChildren().size(); i++) {
                if (node.getChildren().get(i).isLeafNode()) {
                    leafNodes.add(node.getChildren().get(i));
                } else {
                    findLeafNode(node.getChildren().get(i), leafNodes);
                }
            }
        }
    }
}
