package com.dblqt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.*;

@Getter
@RequiredArgsConstructor
@ToString(exclude = "edges")
public class Node {
    private final String name;

    private final List<Package> outbound = new ArrayList<>();

    private final Map<Node, Edge> edges = new HashMap<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return name.equals(node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
