package adventofcode;

import adventofcode.data.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day13 {
    private static final List<List<Node>> NODE_MATRIX = new ArrayList<>();

    public static void main(String[] argv) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/day13.txt"));

        System.out.println(calculateVisibleDots(lines, 1));
        NODE_MATRIX.clear();
        printFoldedPaper(lines);
    }

    private static long calculateVisibleDots(List<String> lines, int foldsToPerform) {
        List<List<Node>> getFoldedPaper = getFoldedPaper(lines, foldsToPerform);

        return getFoldedPaper.stream().map(row -> row.stream().filter(Node::hasDot).count()).reduce(Long::sum).orElseThrow();
    }

    private static void printFoldedPaper(List<String> lines) {
        List<List<Node>> getFoldedPaper = getFoldedPaper(lines);

        for (var row : getFoldedPaper) {
            for (var node : row) {
                System.out.print(node);
            }
            System.out.println();
        }
    }

    private static List<List<Node>> getFoldedPaper(List<String> lines) {
        return getFoldedPaper(lines, Integer.MAX_VALUE);
    }

    private static List<List<Node>> getFoldedPaper(List<String> lines, int foldsToPerform) {
        var folds = populateNodeMatrixAndGetFolds(lines);

        return performFolds(folds, foldsToPerform);
    }


    private static List<Fold> populateNodeMatrixAndGetFolds(List<String> lines) {
        Set<Pair<Integer, Integer>> dots = new HashSet<>();
        List<Fold> folds = new ArrayList<>();

        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (var line : lines) {
            if (line.contains("fold")) {
                String verticalOrHorizontal = line.split(" ")[2].split("=")[0];
                int position = Integer.parseInt(line.split(" ")[2].split("=")[1]);
                folds.add(new Fold(verticalOrHorizontal.equals("y"), position));
            } else if (line.contains(",")) {
                int x = Integer.parseInt(line.split(",")[0]);
                int y = Integer.parseInt(line.split(",")[1]);
                dots.add(new Pair<>(x, y));
                maxX = Math.max(x, maxX);
                maxY = Math.max(y, maxY);
            }
        }

        for (int y = 0; y < maxY + 1; y++) {
            NODE_MATRIX.add(new ArrayList<>(maxX));
            for (int x = 0; x < maxX + 1; x++) {
                NODE_MATRIX.get(y).add(new Node(dots.contains(new Pair<>(x, y))));
            }
        }
        return folds;
    }

    private static List<List<Node>> performFolds(List<Fold> folds, int maxFoldsToPerform) {
        List<List<Node>> oldMatrix = NODE_MATRIX;

        for (int foldNumber = 0; foldNumber < maxFoldsToPerform && foldNumber < folds.size(); foldNumber++) {
            List<List<Node>> newNodeMatrix = new ArrayList<>();
            Fold fold = folds.get(foldNumber);
            int position = fold.position();

            if (fold.vertical()) {
                int numberOfRowsToFold = oldMatrix.size() - position - 1;
                int firstRowAffected = position - numberOfRowsToFold;

                // Initialize the folded area
                for (int i = 0; i < position; i++) {
                    List<Node> row = new ArrayList<>();
                    for (int j = 0; j < oldMatrix.get(i).size(); j++) {
                        row.add(new Node(oldMatrix.get(i).get(j).hasDot));
                    }
                    newNodeMatrix.add(row);
                }

                for (int i = firstRowAffected; i < position; i++) {
                    for (int j = 0; j < newNodeMatrix.get(i).size(); j++) {
                        newNodeMatrix.get(i).set(j, new Node(oldMatrix.get(i).get(j).hasDot || oldMatrix.get(oldMatrix.size() - 1 - (i - firstRowAffected)).get(j).hasDot));
                    }
                }
            } else {
                int numberOfColumnsToFold = oldMatrix.get(0).size() - position - 1;
                int firstColumnAffected = position - numberOfColumnsToFold;

                // Initialize the folded area
                for (List<Node> row : oldMatrix) {
                    List<Node> newRow = new ArrayList<>();
                    for (int j = 0; j < position; j++) {
                        newRow.add(new Node(row.get(j).hasDot));
                    }
                    newNodeMatrix.add(newRow);
                }

                for (int i = 0; i < oldMatrix.size(); i++) {
                    for (int j = firstColumnAffected; j < position; j++) {
                        newNodeMatrix.get(i).set(j, new Node(oldMatrix.get(i).get(j).hasDot || oldMatrix.get(i).get(oldMatrix.get(i).size() - 1 - (j - firstColumnAffected)).hasDot));
                    }
                }
            }

            oldMatrix = newNodeMatrix;
        }
        return oldMatrix;
    }

    public record Node(boolean hasDot) {
        @Override
        public String toString() {
            return hasDot ? "#" : " ";
        }
    }

    public record Fold(boolean vertical, int position) {
    }
}
