package com.dblqt.algorithms;

import com.dblqt.model.Move;
import com.dblqt.model.Node;
import com.dblqt.model.Path;
import com.dblqt.model.Problem;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.dblqt.algorithms.GraphAlgorithms.findShortestPathsDFS;
import static com.dblqt.algorithms.ProblemChecker.checkProblem;
import static com.dblqt.common.ProblemLoader.loadProblem;

@Slf4j
public class Algorithm1 {
    public static Path findNodeWithPackageToDeliver(final Node start) {
        return null;
    }

    public static void executeMoves(final List<Move> moves, final int time) {
        for (var move: moves) {
            var localTime = time - move.getStartTime();
            var loc = move.getDestination().getLocation(localTime);
            var nextEdge = move.getDestination().getEdge(localTime);
            if (loc != null) {

                System.out.printf("@%d, n = %s, q = %s, load= { %s }, drop= { %s }, moving %s->%s:%s arr %d",
                    time, loc.getName(), move.getTain().getName(), "TODO", "TODO", nextEdge.getStart().getName(),
                        nextEdge.getEnd().getName(), nextEdge.getName(), nextEdge.getDistance());
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
            for (var t: problem.getTrains()) {
                var node = t.getLocation();
                // The train is currently travelling.
                if (node == null) {
                    continue;
                }

                if (!node.getOutbound().isEmpty()) {
                    // Does this node have any packages?
                    var packages = node.getOutbound();
                    var l = packages.size();
                    for (int i = 0; i < l; ++i) {
                        var packge = packages.get(i);
                        if (packge.getWeight() <= t.getCapacity()) {
                            t.getCargo().add(packge);
                            t.setLocation(null);
                            packages.remove(i);
                            moves.add(new Move(t, findShortestPathsDFS(node, packge.getDestination()), time));
                            break;
                        }
                    }
                } else {
                    // Move train to the next package
                }
            }

            executeMoves(moves, time);

            ++time;
            break;
        }
    }
}
