package adventofcode;

import adventofcode.data.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day9 {

    public static void main(String[] argv) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/day9.txt"));
        List<List<Integer>> heightMap = lines.stream().map(String::chars)
                .map(stream -> stream.mapToObj(e -> Integer.parseInt(Character.toString(e))).toList())
                .toList();

        System.out.println(countRiskLevel(heightMap));
        System.out.println(calculateProductOfThreeLargestBasins(heightMap));
    }

    public static long countRiskLevel(List<List<Integer>> heightMap) {
        long riskLevel = 0;
        for (int i = 0; i < heightMap.size(); i++) {
            for (int j = 0; j < heightMap.get(i).size(); j++) {
                if (isUpLarger(i, j, heightMap) && isDownLarger(i, j, heightMap)
                        && isLeftLarger(i, j, heightMap) && isRightLarger(i, j, heightMap)) {
                    riskLevel += 1 + heightMap.get(i).get(j);

                }
            }
        }
        return riskLevel;
    }

    private static boolean isUpLarger(int i, int j, List<List<Integer>> heightMap) {
        return i == 0 || heightMap.get(i).get(j) < heightMap.get(i - 1).get(j);
    }

    private static boolean isDownLarger(int i, int j, List<List<Integer>> heightMap) {
        return i == heightMap.size() - 1 || heightMap.get(i).get(j) < heightMap.get(i + 1).get(j);
    }

    private static boolean isLeftLarger(int i, int j, List<List<Integer>> heightMap) {
        return j == 0 || heightMap.get(i).get(j) < heightMap.get(i).get(j - 1);
    }

    private static boolean isRightLarger(int i, int j, List<List<Integer>> heightMap) {
        return j == heightMap.get(i).size() - 1 || heightMap.get(i).get(j) < heightMap.get(i).get(j + 1);
    }


    private static long calculateProductOfThreeLargestBasins(List<List<Integer>> heightMap) {
        List<Set<Pair<Integer, Integer>>> basins = new ArrayList<>();

        for (int i = 0; i < heightMap.size(); i++) {
            for (int j = 0; j < heightMap.get(i).size(); j++) {
                if (heightMap.get(i).get(j) == 9) {
                    continue;
                }

                Pair<Integer, Integer> curPos = new Pair<>(i, j);
                Set<Pair<Integer, Integer>> basin = basins.stream().filter(b -> b.contains(curPos)).findAny().orElseGet(() -> {
                    Set<Pair<Integer, Integer>> b = new HashSet<>();
                    b.add(curPos);
                    basins.add(b);
                    return b;
                });

                // Add right to basin if not 9
                addPositionToBasinIfRequired(new Pair<>(i, j + 1), j + 1 < heightMap.get(i).size(), heightMap, basin, basins);

                // Add down to basin if not 9
                addPositionToBasinIfRequired(new Pair<>(i + 1, j), i + 1 < heightMap.size(), heightMap, basin, basins);
            }
        }

        List<Integer> largestBasins = basins.stream().map(Set::size).sorted(Comparator.reverseOrder()).toList();

        return ((long) largestBasins.get(0)) * largestBasins.get(1) * largestBasins.get(2);
    }

    private static void addPositionToBasinIfRequired(Pair<Integer, Integer> pos, boolean isInsideBounds,
                                                     List<List<Integer>> heightMap, Set<Pair<Integer, Integer>> basin,
                                                     List<Set<Pair<Integer, Integer>>> basins) {
        if (isInsideBounds && heightMap.get(pos.getFirst()).get(pos.getSecond()) != 9 && !basin.contains(pos)) {
            Optional<Set<Pair<Integer, Integer>>> rightBasin = basins.stream().filter(b -> b.contains(pos)).findAny();
            if (rightBasin.isPresent()) {
                basin.addAll(rightBasin.get());
                rightBasin.get().clear();
            } else {
                basin.add(pos);
            }
        }
    }
}
