package com.dblqt.model;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ToString
public class Path {
    private final Node start;

    private final Set<Node> nodes = new HashSet<>();

    private final List<Edge> path = new ArrayList<>();

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

    public Object copy() {
        try {
            var clone = new Path(this.start);

            clone.nodes.addAll(this.nodes);
            clone.path.addAll(this.path);

            return clone;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
