package adventofcode;

import adventofcode.data.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day5 {

    public static void main(String[] argv) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/day5.txt"));

        System.out.println(calculateVentOverlapPoints(lines, false));
        System.out.println(calculateVentOverlapPoints(lines, true));
    }


    public static long calculateVentOverlapPoints(List<String> lines, boolean allowDiagonals) {
        Map<Pair<Integer, Integer>, Integer> ventMap = new HashMap<>();

        List<Vent> vents = lines.stream().map(line -> new Vent(line, allowDiagonals)).toList();
        vents.forEach(
                vent -> {

                    if (vent.yIncrease > 0) {
                        for (int i = 0; i <= vent.yIncrease; i++) {
                            Pair<Integer, Integer> position = new Pair<>(vent.startPos.getFirst(), vent.startPos.getSecond() + i);
                            ventMap.put(position, ventMap.getOrDefault(position, 0) + 1);
                        }
                    } else if (vent.yIncrease < 0) {
                        for (int i = 0; i <= Math.abs(vent.yIncrease); i++) {
                            Pair<Integer, Integer> position = new Pair<>(vent.startPos.getFirst(), vent.startPos.getSecond() - i);
                            ventMap.put(position, ventMap.getOrDefault(position, 0) + 1);
                        }
                    }

                    if (vent.xIncrease > 0) {
                        for (int i = 0; i <= vent.xIncrease; i++) {
                            Pair<Integer, Integer> position = new Pair<>(vent.startPos.getFirst() + i, vent.startPos.getSecond());
                            ventMap.put(position, ventMap.getOrDefault(position, 0) + 1);
                        }
                    } else if (vent.xIncrease < 0) {
                        for (int i = 0; i <= Math.abs(vent.xIncrease); i++) {
                            Pair<Integer, Integer> position = new Pair<>(vent.startPos.getFirst() - i, vent.startPos.getSecond());
                            ventMap.put(position, ventMap.getOrDefault(position, 0) + 1);
                        }
                    }

                    if (vent.bothXyIncrease > 0) {
                        for (int i = 0; i <= vent.bothXyIncrease; i++) {
                            Pair<Integer, Integer> position = new Pair<>(vent.startPos.getFirst() + i, vent.startPos.getSecond() + i);
                            ventMap.put(position, ventMap.getOrDefault(position, 0) + 1);
                        }
                    } else if (vent.bothXyIncrease < 0) {
                        for (int i = 0; i <= Math.abs(vent.bothXyIncrease); i++) {
                            Pair<Integer, Integer> position = new Pair<>(vent.startPos.getFirst() - i, vent.startPos.getSecond() - i);
                            ventMap.put(position, ventMap.getOrDefault(position, 0) + 1);
                        }
                    }

                    if (vent.xIncreaseYDecrease > 0) {
                        for (int i = 0; i <= vent.xIncreaseYDecrease; i++) {
                            Pair<Integer, Integer> position = new Pair<>(vent.startPos.getFirst() + i, vent.startPos.getSecond() - i);
                            ventMap.put(position, ventMap.getOrDefault(position, 0) + 1);
                        }
                    } else if (vent.xIncreaseYDecrease < 0) {
                        for (int i = 0; i <= Math.abs(vent.xIncreaseYDecrease); i++) {
                            Pair<Integer, Integer> position = new Pair<>(vent.startPos.getFirst() - i, vent.startPos.getSecond() + i);
                            ventMap.put(position, ventMap.getOrDefault(position, 0) + 1);
                        }
                    }
                }
        );

        return ventMap.entrySet().stream().filter(entry -> entry.getValue() > 1).count();
    }

    public static class Vent {
        final Pair<Integer, Integer> startPos;
        final Integer xIncrease;
        final Integer yIncrease;
        final Integer bothXyIncrease;
        final Integer xIncreaseYDecrease;

        public Vent(String s, boolean allowDiagonals) {
            String start = s.split(" -> ")[0];
            String end = s.split(" -> ")[1];

            Integer startX = Integer.valueOf(start.split(",")[0]);
            Integer startY = Integer.valueOf(start.split(",")[1]);

            Integer endX = Integer.valueOf(end.split(",")[0]);
            Integer endY = Integer.valueOf(end.split(",")[1]);

            startPos = new Pair<>(startX, startY);

            if (startX.equals(endX)) {
                yIncrease = endY - startY;
                xIncrease = 0;
                bothXyIncrease = 0;
                xIncreaseYDecrease = 0;
            } else if (startY.equals(endY)) {
                xIncrease = endX - startX;
                yIncrease = 0;
                bothXyIncrease = 0;
                xIncreaseYDecrease = 0;
            } else if (allowDiagonals && (endX - startX == endY - startY)) { // diagonal right (1,1) (3,3) & (3,3) (1,1)
                xIncrease = 0;
                yIncrease = 0;
                bothXyIncrease = endX - startX;
                xIncreaseYDecrease = 0;
            } else if (allowDiagonals && (startX - endX == endY - startY)) { // diagonal left (1,3) (3,1) & (3,1) (1,3)
                xIncrease = 0;
                yIncrease = 0;
                xIncreaseYDecrease = endX - startX;
                bothXyIncrease = 0;
            } else {
                xIncrease = 0;
                yIncrease = 0;
                bothXyIncrease = 0;
                xIncreaseYDecrease = 0;
            }
        }
    }
}
