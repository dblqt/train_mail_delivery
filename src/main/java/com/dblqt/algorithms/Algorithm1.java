package com.dblqt.algorithms;

import com.dblqt.model.*;
import com.dblqt.model.Package;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.dblqt.algorithms.GraphAlgorithms.findClosesPackageBFS;
import static com.dblqt.algorithms.GraphAlgorithms.findShortestPathsDFS;
import static com.dblqt.algorithms.ProblemChecker.checkProblem;
import static com.dblqt.common.PrettyFormat.formatPackages;
import static com.dblqt.common.ProblemLoader.loadProblem;

@Slf4j
public class Algorithm1 {
    public static Path findNodeWithPackageToDeliver(final Node start) {
        return null;
    }

    public static void executeMoves(final List<Move> moves, final int time) {
        for (var move: moves) {
            var localTime = move.getStartTime();
            if (localTime == time) {
                final var currentEdge = move.getSegment();
                final var loc = move.getStart();

                final var edgeS = currentEdge != null
                        ? String.format("%s->%s:%s", currentEdge.getStart().getName(), currentEdge.getEnd().getName(),
                            currentEdge.getName())
                        : "nil";

                System.out.printf("@%d, n = %s, q = %s, load= { %s }, drop= { %s }, moving %s arr %d\n",
                    time, loc.getName(), move.getTrain().getName(), formatPackages(move.getLoad()),
                        formatPackages(move.getDrop()), edgeS, move.getExpectedArrivalTime());
            }
        }
    }

    public static void main(String... args) throws Exception {
        log.info("Executing Algorithm 1 ...");

        final Problem problem = loadProblem("./samples/examples.txt");

        checkProblem(problem);

        System.out.println(problem);
        var time = 0;
        var moves = new ArrayList<Move>();
        while(problem.undeliveredPackages()) {
            log.debug("Time step: {}", time);
            processCompletedMoves(moves, time);

            for (var t: problem.getTrains()) {
                var node = t.getLocation();

                if (node == null) {
                    // Train is currently between 2 nodes.
                    continue;
                }

                // Deliver packages if any.
                var deliveredPackages = deliverPackage(node, t.getCargo());

                if (t.getVoyage() != null && t.getVoyage().getEndTime() != time) {
                    // The train is currently on route, we will simply continue the journey until we arrive at the
                    // final destination.
                    var nextEdge = t.getVoyage().getNextEdge(time);
                    var move = new Move(t, nextEdge.getStart(), t.getVoyage().getPath(), time, nextEdge, time + nextEdge.getDistance());
                    // move.getLoad().addAll(t.getCargo());
                    move.getDrop().addAll(deliveredPackages);
                    moves.add(move);
                    continue;
                }

                if (!node.getOutbound().isEmpty()) {
                    // Does this node have any packages?
                    var packages = node.getOutbound();
                    var l = packages.size();
                    for (int i = 0; i < l; ++i) {
                        var packge = packages.get(i);
                        if (packge.getWeight() <= t.getCapacity()) {
                            var load = new HashSet<Package>();
                            load.add(packge);
                            t.getCargo().addAll(load);
                            t.setLocation(null);
                            packages.remove(i);
                            final Path shortestPathsDFS = findShortestPathsDFS(node, packge.getDestination());
                            final Edge nextEdge = shortestPathsDFS.getPath().get(0);
                            final Move move = new Move(t, nextEdge.getStart(), shortestPathsDFS, time, nextEdge,
                                    time + nextEdge.getDistance());
                            move.getLoad().addAll(load);
                            move.getDrop().addAll(deliveredPackages);
                            moves.add(move);
                            final Voyage voyage = new Voyage(shortestPathsDFS, time,
                                    time + shortestPathsDFS.getLength());
                            t.setVoyage(voyage);
                            // moves.ad
                            break;
                        }
                    }
                } else {
                    // If there are no more undelivered packages we want to show the final move.
                    if (!problem.undeliveredPackages()) {
                        var move = new Move(t, t.getLocation(), null, time, null, time);
                        move.getDrop().addAll(deliveredPackages);
                        moves.add(move);
                        continue;
                    }

                    // Move train to the next package
                    var nextPackage = findClosesPackageBFS(t.getLocation());
                    if (nextPackage.isPresent()) {
                        t.setLocation(null);
                        final Path shortestPathsDFS = findShortestPathsDFS(node, nextPackage.get());
                        final Edge nextEdge = shortestPathsDFS.getPath().get(0);
                        final Move move = new Move(t, nextEdge.getStart(), shortestPathsDFS, time, nextEdge,
                                time + nextEdge.getDistance());
                        move.getDrop().addAll(deliveredPackages);
                        moves.add(move);

                        final Voyage voyage = new Voyage(shortestPathsDFS, time,
                                time + shortestPathsDFS.getLength());

                        t.setVoyage(voyage);
                    }
                }
            }

            executeMoves(moves, time);

            ++time;
            // break;
        }
    }

    private static void processCompletedMoves(ArrayList<Move> moves, int time) {
        for (var move: new ArrayList<>(moves)) {
            if (move.getExpectedArrivalTime() == time) {
                move.getTrain().setLocation(move.getSegment().getEnd());
                moves.remove(move);
            }
        }
    }

    private static Set<Package> deliverPackage(final Node location, Collection<Package> load) {
        final var delivered = new HashSet<Package>();

        for (var pkg: load) {
            if (pkg.getDestination().equals(location)) {
                pkg.setDelivered(true);
                delivered.add(pkg);
            }
        }

        load.removeAll(delivered);

        return delivered;
    }
}
