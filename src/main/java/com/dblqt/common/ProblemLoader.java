package com.dblqt.common;

import com.dblqt.model.Edge;
import com.dblqt.model.Node;
import com.dblqt.model.Package;
import com.dblqt.model.Problem;
import com.dblqt.model.Train;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

@Slf4j
public class ProblemLoader {
    @Getter
    private static class Parser {
        private enum ParseState {
            STATIONS, ROUTES, DELIVERIES, TRAINS, COMPLETE;

            private static final ParseState[] vals = values();

            public ParseState next()
            {
                return vals[(this.ordinal()+1) % vals.length];
            }
        }

        private ParseState state = ParseState.STATIONS;
        private boolean skipEmpty = true;
        private final Problem problem = new Problem();
        private int expectedLines = 0;

        private int lineCount = 0;

        private int readCount(String line) {
            var p = Pattern.compile("^(\\d+).*");

            var match = p.matcher(line);
            if (match.matches()) {
                var m = match.group(1);
                return Integer.parseInt(m);
            }

            throw new RuntimeException(
                    String.format("Found invalid count row: %s", line)
            );
        }

        private void readStations(String line) {
            var p = Pattern.compile("^(\\w+).*");

            var match = p.matcher(line);
            if (match.matches()) {
                var m = match.group(1);
                final Node node = new Node(m);
                problem.getNodeMap().put(m, node);
                return;
            }

            throw new RuntimeException(
                    String.format("Found invalid count row: %s", line)
            );
        }

        private void readTrains(String line) {
            var p = Pattern.compile("^(\\w+).*,(\\w+),(\\d).*");

            var match = p.matcher(line);
            if (match.matches()) {
                var n = match.group(1);
                var location = problem.getNodeMap().get(match.group(2));
                var capacity = Integer.parseInt(match.group(3));

                var train = new Train(n, capacity);

                train.setLocation(location);

                problem.getTrains().add(train);

                return;
            }

            throw new RuntimeException(
                    String.format("Found invalid count row: %s", line)
            );
        }

        private void readPackages(String line) {
            var p = Pattern.compile("^(\\w+),(\\w+),(\\w+),(\\d+).*");

            var match = p.matcher(line);
            if (match.matches()) {
                var n = match.group(1);
                var start = problem.getNodeMap().get(match.group(2));
                var destination = problem.getNodeMap().get(match.group(3));
                var weight = Integer.parseInt(match.group(4));

                var packge = new Package(n, problem.getNodeMap().get(destination.getName()), weight);

                start.getOutbound().add(packge);
                problem.getPackages().add(packge);

                return;
            }

            throw new RuntimeException(
                    String.format("Found invalid count row: %s", line)
            );
        }

        private void readRoutes(String line) {
            var p = Pattern.compile("^(\\w+),(\\w+),(\\w+),(\\d+).*");

            var match = p.matcher(line);
            if (match.matches()) {
                var n = match.group(1);
                var start = problem.getNodeMap().get(match.group(2));
                var stop = problem.getNodeMap().get(match.group(3));
                var length = Integer.parseInt(match.group(4));

                var edge1 = new Edge(n, start, stop, length);
                problem.getEdges().add(edge1);
                start.getEdges().put(stop, edge1);

                var edge2 = new Edge(n, stop, start, length);
                problem.getEdges().add(edge2);
                stop.getEdges().put(start, edge2);

                return;
            }

            throw new RuntimeException(
                    String.format("Found invalid count row: %s", line)
            );
        }

        private static boolean isEmpty(String line) {
            return (line.equals("") || line.startsWith("//"));
        }

        public void parse(String line) {
            ++lineCount;
            try {
                log.debug("Reading Line: {}", line);
                var clean = line.trim();

                if (skipEmpty && isEmpty(clean)) {
                    log.debug("Skipping empty line ...");
                    return;
                }

                if (!skipEmpty && expectedLines != 0 && isEmpty(clean)) {
                    throw new RuntimeException("Invalid file format, no empty lines in sections are allowed.");
                }

                if (!skipEmpty && expectedLines == 0 && isEmpty(clean)) {
                    state = state.next();
                    skipEmpty = true;
                    return;
                }

                if (expectedLines == 0) {
                    expectedLines = readCount(clean);
                    skipEmpty = false;
                    return;
                }

                switch (state) {
                    case STATIONS:
                        readStations(line);
                        break;
                    case ROUTES:
                        readRoutes(line);
                        break;
                    case DELIVERIES:
                        readPackages(line);
                        break;
                    case TRAINS:
                        readTrains(line);
                        break;
                    default:
                        throw new RuntimeException("Found unexpected content.");
                }

                --expectedLines;

            } catch (Throwable t) {
                log.error("An error occured at line {} : {}", lineCount, line);
                throw t;
            }
        }
    }

    public static Problem loadProblem(String filePath) throws IOException {
        var parser = new Parser();

        Files.lines(Path.of(filePath)).forEach(parser::parse);

        return parser.getProblem();
    }
}
