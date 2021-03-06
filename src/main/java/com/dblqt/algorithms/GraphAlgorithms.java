package com.dblqt.algorithms;

import com.dblqt.model.Edge;
import com.dblqt.model.Node;
import com.dblqt.model.Path;

import java.util.*;
import java.util.stream.Collectors;

public class GraphAlgorithms {

    public static List<Path> findAllPathsDFS(final Node from, final Node to) {
        if (from.equals(to)) {
            throw new RuntimeException("Paths where start and end are the same are not supported.");
        }

        var firstPath = new Path(from);
        var candidates = new LinkedList<>(Collections.singleton(firstPath));

        var result = new ArrayList<Path>();
        while (!candidates.isEmpty()) {
            var path = candidates.pop();

            for (var e: path.getEnd().getEdges().values()) {
                var nextEnd = e.getEnd();
                if (path.getNodes().contains(nextEnd)) {
                    continue;
                }
                var clone = (Path)path.copy();
                clone.getNodes().add(nextEnd);
                clone.getPath().add(e);

                if (nextEnd.equals(to)) {
                    result.add(clone);
                } else {
                    candidates.push(clone);
                }
            }
        }

        return result;
    }

    public static Path findShortestPathsDFS(final Node from, final Node to) {
        if (from.equals(to)) {
            throw new RuntimeException("Paths where start and end are the same are not supported.");
        }

        var paths = findAllPathsDFS(from, to);

        return paths.stream().min(Comparator.comparingInt(Path::getLength)).orElseThrow();
    }

    public static Optional<Node> findClosesPackageBFS(final Node start, final int capacity) {
        return findClosesPackageBFS(start, capacity, new HashSet<>());
    }

    public static Optional<Node> findClosesPackageBFS(final Node start, final int capacity,
                                                      final HashSet<Node> blackList) {
        if (start.getOutbound().size() > 0) {
            if (start.getOutbound().stream().anyMatch(p -> p.getWeight() <= capacity))
            return Optional.of(start);
        }

        var openNodes = new LinkedList<>(Collections.singleton(start));
        var closedNodes = new HashSet<>();

        while (!openNodes.isEmpty()) {
            var node = openNodes.pop();
            if (!closedNodes.add(node)) {
                continue;
            }

            var edges = node.getEdges().values().stream()
                    .sorted(Comparator.comparing(Edge::getDistance))
                    .collect(Collectors.toList());

            for (var edge: edges) {
                var end = edge.getEnd();

                for (var pkg: end.getOutbound()) {
                    if (pkg.getWeight() <= capacity && !blackList.contains(end)) {
                        return Optional.of(end);
                    }
                }

                openNodes.add(end);
            }
        }

        return Optional.empty();
    }
}
