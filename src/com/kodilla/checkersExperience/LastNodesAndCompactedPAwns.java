package com.kodilla.checkersExperience;

import com.kodilla.tree.Node;
import com.kodilla.view.Pawn;

import java.util.ArrayList;
import java.util.List;

public class LastNodesAndCompactedPAwns {

    public static List<Pawn> findCompactedPawns(Node<PositionAndKilledPawn> lastNode) {
        List<Pawn> compactedPawns = new ArrayList<>();
        if (lastNode != null) {
            Node<PositionAndKilledPawn> node = lastNode;
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

    public static void findLeafNode(Node<PositionAndKilledPawn> node, List<Node<PositionAndKilledPawn>> leafNodes) {
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
