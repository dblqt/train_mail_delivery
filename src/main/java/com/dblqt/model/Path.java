package com.dblqt.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
public class Path implements Cloneable {
    private final Node start;

    private final Set<Node> nodes = new HashSet<>();

    private final List<Edge> path = new ArrayList<>();

    public Node getLocation(final int time) {
        if (time == 0) {
            return start;
        }

        var localTime = time;
        for (var edge: path) {
            localTime -= edge.getDistance();

            if (localTime < 0 ) {
                return null;
            }

            return edge.getEnd();
        }

        return null;
        // The train has arived.
    }

    public Edge getEdge(final int time) {
        if (time == 0) {
            return path.get(0);
        }

        var localTime = time;
        for (var edge: path) {
            localTime -= edge.getDistance();

            if (localTime < 0 ) {
                return edge;
            }
        }

        return null;
        // The train has arived.
    }

    public Node getEnd() {
        if (path.size() == 0 && nodes.size() != 1) {
            throw new RuntimeException("The path is incorrectly configured.");
        }

        if (nodes.size() == 1) {
            return nodes.stream().findFirst().orElseThrow();
        }

        return path.get(path.size() - 1).getEnd();
    }

    public Path(final Node start) {
        this.start = start;
        nodes.add(start);
    }

    public int getLength() {
        return path.stream().mapToInt(Edge::getDistance).sum();
    }

    public Object clone() {
        try {
            var clone = (Path) super.clone();

            clone.nodes.addAll(this.nodes);
            clone.path.addAll(this.path);

            return clone;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
