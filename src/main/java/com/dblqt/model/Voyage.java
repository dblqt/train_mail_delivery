package com.dblqt.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Voyage {
    private final Path path;

    private final int startTime;

    private final int endTime;

/*
    public Node getLocation(final int time) {
        if (time == startTime) {
            return path.getStart();
        }

        var localTime = time - startTime;
        for (var edge: path.getPath()) {
            localTime -= edge.getDistance();

            if (localTime < 0 ) {
                return null;
            }

            return edge.getEnd();
        }

        return null;
        // The train has arived.
    }
*/

/*
    public Edge getEdge(final int time) {
        if (time == startTime) {
            return path.getPath().get(0);
        }

        var localTime = time;
        for (var edge: path.getPath()) {
            localTime -= edge.getDistance();

            if (localTime < 0 ) {
                return edge;
            }
        }

        return null;
        // The train has arived.
    }
*/

    public Edge getNextEdge(final int time) {
        if (time == startTime) {
            return path.getPath().get(0);
        }

        var localTime = time - startTime;
        for (var edge: path.getPath()) {
            if (localTime == 0 ) {
                return edge;
            }
            localTime -= edge.getDistance();
        }

        return null;
        // The train has arived.
    }

    public boolean isEnRoute(final Node node, int time) {
        var localTime = time - startTime;
        for (var edge: path.getPath()) {
            if (localTime > 0 ) {
                localTime -= edge.getDistance();
            }

            if (edge.getEnd().equals(node)) {
                return true;
            }
        }
        return false;
    }
}

