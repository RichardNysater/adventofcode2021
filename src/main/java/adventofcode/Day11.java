package adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day11 {

    private static final long TOTAL_NUMBER_OF_OCTOPUSES = 100;

    public static void main(String[] argv) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/day11.txt"));

        System.out.println(calculateTotalFlashes(lines, 100));
        System.out.println(calculateFirstStepWhereAllOctopusesFlash(lines));
    }

    private static long calculateTotalFlashes(List<String> lines, int steps) {
        List<List<DumboOctopus>> octopusMap = constructOctopusMap(lines);

        long totalFlashes = 0;

        for (int step = 0; step < steps; step++) {
            newStep(octopusMap);
            increaseAllOctopusEnergy(octopusMap);

            boolean changeMade = true;
            while (changeMade) {
                long newFlashes = flashOctopushesThatShouldFlash(octopusMap);
                changeMade = newFlashes > 0;
                totalFlashes += newFlashes;
            }
        }

        return totalFlashes;
    }


    private static long calculateFirstStepWhereAllOctopusesFlash(List<String> lines) {
        List<List<DumboOctopus>> octopusMap = constructOctopusMap(lines);
        long stepWhereAllOctopusesFlash = 0;
        while (true) {
            newStep(octopusMap);
            stepWhereAllOctopusesFlash++;

            increaseAllOctopusEnergy(octopusMap);

            boolean changeMade = true;
            int flashesThisStep = 0;
            while (changeMade) {
                long newFlashes = flashOctopushesThatShouldFlash(octopusMap);
                changeMade = newFlashes > 0;
                flashesThisStep += newFlashes;

            }

            if (flashesThisStep == TOTAL_NUMBER_OF_OCTOPUSES) {
                return stepWhereAllOctopusesFlash;
            }
        }
    }

    private static List<List<DumboOctopus>> constructOctopusMap(List<String> lines) {
        return lines.stream()
                .map(String::chars)
                .map(stream -> stream.mapToObj(c -> new DumboOctopus(Integer.parseInt(String.valueOf((char) c)))).toList())
                .toList();
    }

    private static void newStep(List<List<DumboOctopus>> octopusMap) {
        for (int i = 0; i < octopusMap.size(); i++) {
            for (int j = 0; j < octopusMap.size(); j++) {
                octopusMap.get(i).get(j).newStep();
            }
        }
    }

    private static long flashOctopushesThatShouldFlash(List<List<DumboOctopus>> octopusMap) {
        long flashes = 0;
        for (int i = 0; i < octopusMap.size(); i++) {
            for (int j = 0; j < octopusMap.size(); j++) {
                if (octopusMap.get(i).get(j).shouldFlash()) {
                    octopusMap.get(i).get(j).flash();
                    flashes++;

                    tryIncreaseEnergy(i + 1, j, octopusMap);
                    tryIncreaseEnergy(i - 1, j, octopusMap);
                    tryIncreaseEnergy(i, j + 1, octopusMap);
                    tryIncreaseEnergy(i, j - 1, octopusMap);
                    tryIncreaseEnergy(i + 1, j + 1, octopusMap);
                    tryIncreaseEnergy(i + 1, j - 1, octopusMap);
                    tryIncreaseEnergy(i - 1, j + 1, octopusMap);
                    tryIncreaseEnergy(i - 1, j - 1, octopusMap);
                }
            }
        }
        return flashes;
    }

    private static void increaseAllOctopusEnergy(List<List<DumboOctopus>> octopusMap) {
        for (int i = 0; i < octopusMap.size(); i++) {
            for (int j = 0; j < octopusMap.size(); j++) {
                tryIncreaseEnergy(i, j, octopusMap);
            }
        }
    }

    private static void tryIncreaseEnergy(int i, int j, List<List<DumboOctopus>> octopusMap) {
        if (i >= 0 && j >= 0 && i < octopusMap.size() && j < octopusMap.get(i).size()) {
            octopusMap.get(i).get(j).increaseEnergy();
        }
    }

    public static class DumboOctopus {
        private int charge;
        private boolean shouldFlash = false;
        private boolean hasFlashed = false;

        public DumboOctopus(int initialCharge) {
            charge = initialCharge;
        }

        public void increaseEnergy() {
            if (!shouldFlash) {
                charge++;
                if (charge > 9) {
                    shouldFlash = true;
                    charge = 0;
                }
            }
        }

        public boolean shouldFlash() {
            return shouldFlash && !hasFlashed;
        }

        public void flash() {
            hasFlashed = true;
        }

        public void newStep() {
            shouldFlash = false;
            hasFlashed = false;
        }
    }
}
