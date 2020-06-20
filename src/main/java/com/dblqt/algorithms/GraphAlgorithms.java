package com.dblqt.algorithms;

import com.dblqt.model.Node;
import com.dblqt.model.Path;

import java.util.*;

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
                var clone = (Path)path.clone();
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
}
