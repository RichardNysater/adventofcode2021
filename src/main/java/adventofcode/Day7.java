package adventofcode;

import adventofcode.data.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day7 {
    private static final Map<Pair<Long, Long>, Long> fuelConsumptionByCrabPosition = new HashMap<>();

    public static void main(String[] argv) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/day7.txt"));
        System.out.println(calculateMinimumFuelConsumption(lines.get(0)));
        System.out.println(calculateCrabMinimumFuelConsumption(lines.get(0)));
    }

    public static long calculateMinimumFuelConsumption(String crabPositionString) {
        List<Long> crabPositions = Arrays.stream(crabPositionString.split(",")).map(Long::parseLong).toList();
        long minPos = crabPositions.stream().reduce(Long::min).orElseThrow();
        long maxPos = crabPositions.stream().reduce(Long::max).orElseThrow();

        long minFuelConsumption = Long.MAX_VALUE;
        for (long i = minPos; i <= maxPos; i++) {
            Long fuelConsumption = calculateFuelConsumption(crabPositions, i);
            minFuelConsumption = minFuelConsumption > fuelConsumption ? fuelConsumption : minFuelConsumption;
        }
        return minFuelConsumption;
    }

    private static Long calculateFuelConsumption(List<Long> crabPositions, Long position) {
        return crabPositions.stream().map(crabPosition -> Math.abs(crabPosition - position)).reduce(Long::sum).orElseThrow();
    }

    public static long calculateCrabMinimumFuelConsumption(String crabPositionString) {
        List<Long> crabPositions = Arrays.stream(crabPositionString.split(",")).map(Long::parseLong).toList();
        long minPos = crabPositions.stream().reduce(Long::min).orElseThrow();
        long maxPos = crabPositions.stream().reduce(Long::max).orElseThrow();

        long minFuelConsumption = Long.MAX_VALUE;
        for (long i = minPos; i <= maxPos; i++) {
            Long fuelConsumption = calculateCrabFuelConsumption(crabPositions, i);
            minFuelConsumption = minFuelConsumption > fuelConsumption ? fuelConsumption : minFuelConsumption;
        }
        return minFuelConsumption;
    }

    private static Long calculateCrabFuelConsumption(List<Long> crabPositions, Long position) {
        return crabPositions.stream().map(crabPosition -> calculateCrabFuelConsumption(new Pair<>(crabPosition, position))).reduce(Long::sum).orElseThrow();
    }

    private static long calculateCrabFuelConsumption(Pair<Long, Long> positions) {
        boolean hasKey = fuelConsumptionByCrabPosition.containsKey(positions);
        if (hasKey) {
            return fuelConsumptionByCrabPosition.get(positions);
        } else {
            long consumption = 0L;
            for (int i = 0; i <= Math.abs(positions.getFirst() - positions.getSecond()); i++) {
                consumption += i;
            }
            fuelConsumptionByCrabPosition.put(positions, consumption);
            return consumption;
        }
    }
}
