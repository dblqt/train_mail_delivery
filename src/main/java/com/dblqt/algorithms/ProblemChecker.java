package com.dblqt.algorithms;

import com.dblqt.model.Node;
import com.dblqt.model.Package;
import com.dblqt.model.Problem;
import com.dblqt.model.Train;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

public class ProblemChecker {
    private static void allPackagesCanBeDelivered(final Problem problem) {
        int maxCapacity = problem.getTrains().stream().map(Train::getCapacity).max(Integer::compareTo).orElseThrow();
        int maxWeight = problem.getPackages().stream().map(Package::getWeight).max(Integer::compareTo).orElseThrow();

        if (maxWeight > maxCapacity) {
            throw new RuntimeException("The problem is not solvable. There is at least one package" +
                    " that cannot fit onto a train.");
        }
    }

    private static void isConnected(final Problem problem) {
        var allNodes = new HashSet<>(problem.getNodeMap().values());
        var firstNode = Collections.singletonList(problem.getTrains().get(0).getLocation());
        var openNodes = new LinkedList<>(firstNode);
        var closedNodes = new HashSet<Node>();

        while (!openNodes.isEmpty()) {
            var current = openNodes.pop();
            closedNodes.add(current);
            allNodes.remove(current);

            for (var node: current.getEdges().keySet()) {
                if (closedNodes.contains(node)) {
                    continue;
                }
                openNodes.add(node);
            }
        }

        if (!allNodes.isEmpty()) {
            throw new RuntimeException("Partitioned graphs are not supported.");
        }
    }

    public static void checkProblem(final Problem problem) {
        isConnected(problem);

        allPackagesCanBeDelivered(problem);
    }
}
