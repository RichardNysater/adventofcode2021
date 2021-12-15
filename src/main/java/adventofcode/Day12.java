package adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day12 {
    private static final String START_NAME = "start";
    private static final String END_NAME = "end";
    private static final Set<List<String>> FOUND_PATHS = new HashSet<>();

    public static void main(String[] argv) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/day12.txt"));

        System.out.println(calculateAllPossiblePaths(lines, false).size());

        FOUND_PATHS.clear();
        System.out.println(calculateAllPossiblePaths(lines, true).size());
    }

    private static Set<List<String>> calculateAllPossiblePaths(List<String> lines, boolean canVisitSmallNodeTwiceOneTime) {
        Map<String, Node> nodeByName = new HashMap<>();

        lines.forEach(
                line -> {
                    String start = line.split("-")[0];
                    String end = line.split("-")[1];

                    Node startNode = nodeByName.getOrDefault(start, createNode(start));
                    Node endNode = nodeByName.getOrDefault(end, createNode(end));

                    startNode.connections().add(endNode);
                    endNode.connections().add(startNode);

                    nodeByName.put(startNode.name(), startNode);
                    nodeByName.put(endNode.name(), endNode);
                }
        );


        calculatePath(nodeByName.get(START_NAME), new ArrayList<>(), new HashSet<>(), !canVisitSmallNodeTwiceOneTime);

        return FOUND_PATHS;
    }

    private static Node createNode(String nodeName) {
        return new Node(new ArrayList<>(), nodeName, nodeName.equals(nodeName.toUpperCase(Locale.ROOT)));
    }

    private static void calculatePath(Node node, List<String> path, Set<String> visitedNodes, boolean smallNodeVisitedTwice) {
        Set<String> visitedNodes2 = new HashSet<>(Set.copyOf(visitedNodes));
        List<String> path2 = new ArrayList<>(List.copyOf(path));

        path2.add(node.name());
        visitedNodes2.add(node.name());
        if (node.name().equals(END_NAME)) {
            FOUND_PATHS.add(path2);
            return;
        }
        for (var potentialNextNode : node.connections().stream()
                .filter(n -> !n.name().equals(START_NAME))
                .filter(n -> n.large || !visitedNodes.contains(n.name()) || (visitedNodes.contains(n.name()) && !smallNodeVisitedTwice))
                .toList()) {

            if (!potentialNextNode.large && visitedNodes.contains(potentialNextNode.name())) {
                if (!smallNodeVisitedTwice) {
                    calculatePath(potentialNextNode, path2, visitedNodes2, true);
                }
            } else {
                calculatePath(potentialNextNode, path2, visitedNodes2, smallNodeVisitedTwice);
            }
        }
    }

    public record Node(List<Node> connections, String name, boolean large) {
    }
}
