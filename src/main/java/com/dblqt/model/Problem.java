package com.dblqt.model;

import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;

@Getter
public class Problem {
    private final Map<String, Node> nodeMap = new HashMap<>();

    private final Map<String, Edge> edgeMap = new HashMap<>();

    private final Map<String, Train> trainMap = new HashMap<>();

    private final Map<String, Package> packageMap = new HashMap<>();

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

    public boolean undeliveredPackages() {
        return packageMap.values().stream().map(Package::isDelivered).anyMatch(x -> !x);
    }

    public void addNode(final Node node) {
        final String name = node.getName();
        if (nodeMap.put(name, node) != null) {
            throw new RuntimeException(
                    String.format("Node already exists: %s", name)
            );
        }
    }

    public void addTrain(final Train train) {
        final String name = train.getName();
        if (trainMap.put(name, train) != null) {
            throw new RuntimeException(
                    String.format("Train already exists: %s", name)
            );
        }
    }

    public void addPackage(final Package pkg) {
        final String name = pkg.getName();
        if (packageMap.put(name, pkg) != null) {
            throw new RuntimeException(
                    String.format("Package already exists: %s", name)
            );
        }
    }

    public void addEdge(final Edge edge) {
        final String name = edge.getName();
        if (edgeMap.put(name, edge) != null) {
            throw new RuntimeException(
                    String.format("Package already exists: %s", name)
            );
        }

        // Add the revers edge.
        edgeMap.put(name+"_R", new Edge(edge.getName(), edge.getEnd(), edge.getStart(), edge.getDistance()));
    }

    public List<Train> getTrains() {
        return new ArrayList<>(trainMap.values());
    }

    public Collection<Package> getPackages() {
        return packageMap.values();
    }

/*
    public Collection<Node> getNodes() {
        return nodeMap.values();
    }
*/
}
