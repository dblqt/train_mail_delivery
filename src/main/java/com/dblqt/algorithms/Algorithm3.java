package com.dblqt.algorithms;

import com.dblqt.model.Package;
import com.dblqt.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashSet;

import static com.dblqt.algorithms.Algorithm1.executeMoves;
import static com.dblqt.algorithms.Algorithm1.processCompletedMoves;
import static com.dblqt.algorithms.GraphAlgorithms.findClosesPackageBFS;
import static com.dblqt.algorithms.GraphAlgorithms.findShortestPathsDFS;
import static com.dblqt.algorithms.ProblemChecker.checkProblem;
import static com.dblqt.common.ProblemLoader.loadProblem;

@Slf4j
public class Algorithm3 {
    public static void main(String... args) throws Exception {
        log.info("Executing Algorithm 3 ...");

        final var problems = new String[] {
/*
                "./samples/sample.txt",
                "./samples/sample-1.txt",
                "./samples/sample-2.txt",
                "./samples/sample-3.txt",
                "./samples/sample-4.txt",
                "./samples/sample-5.txt",
                "./samples/sample-6.txt",
*/
                "./samples/sample-7.txt",
        };

        for (var problemFile: problems) {
            System.out.printf("Solving problem: %s\n", problemFile);
            final var problem = loadProblem(problemFile);
            solveProblem(problem);
        }
    }

    private static void solveProblem(Problem problem) {
        checkProblem(problem);

        // System.out.println(problem);

        var time = 0;
        var moves = new ArrayList<Move>();
        while(problem.undeliveredPackages()) {
            log.debug("Time step: {}", time);
            processCompletedMoves(moves, time);

            var blackList = new HashSet<Node>();
            for (var t: problem.getTrains()) {
                var node = t.getLocation();

                if (node == null) {
                    // Train is currently between 2 nodes.
                    continue;
                }

                // Deliver packages if any.
                var deliveredPackages = t.deliverPackages(node);

                if (t.getVoyage() != null && t.getVoyage().getEndTime() > time) {
                    var outBound = node.getOutbound();
                    var load = new HashSet<Package>();
                    for (var pkg: new ArrayList<>(outBound)) {
                        if (pkg.getWeight() <= t.getCapacity() && t.getVoyage().isEnRoute(pkg.getDestination(), time)) {
                            t.loadPackage(pkg);
                            outBound.remove(pkg);
                            load.add(pkg);
                        }
                    }

                    // The train is currently on route, we will simply continue the journey until we arrive at the
                    // final destination.
                    t.setLocation(null);
                    var nextEdge = t.getVoyage().getNextEdge(time);
                    var move = new Move(t, nextEdge.getStart(), t.getVoyage().getPath(), time, nextEdge, time + nextEdge.getDistance());
                    // move.getLoad().addAll(t.getCargo());
                    move.getDrop().addAll(deliveredPackages);
                    move.getLoad().addAll(load);
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
                            t.loadPackage(packge);
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
                }

                if (t.isEmpty()) {
                    var nextPackage = findClosesPackageBFS(t.getLocation(), t.getCapacity(), blackList);
                    // If there are no more undelivered packages we want to show the final move.
                    if (nextPackage.isEmpty() && deliveredPackages.size() > 0) {
                        var move = new Move(t, t.getLocation(), null, time, null, time);
                        move.getDrop().addAll(deliveredPackages);
                        moves.add(move);
                        continue;
                    }

                    // Move train to the next package
                    if (nextPackage.isPresent()) {
                        t.setLocation(null);
                        final Node destination = nextPackage.get();
                        blackList.add(destination);
                        final Path shortestPathsDFS = findShortestPathsDFS(node, destination);
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
}
