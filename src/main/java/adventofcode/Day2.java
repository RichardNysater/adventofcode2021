package adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day2 {
    public static void main(String[] argv) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/day2.txt"));

        System.out.println(calculateFinalPosition(lines));
        System.out.println(calculateFinalPositionUsingAim(lines));
    }

    /**
     * Part 1
     */
    private static int calculateFinalPosition(List<String> input) {
        int horizontalPosition = 0;
        int depthPosition = 0;

        for (String commandLine : input) {
            String command = commandLine.split(" ")[0];
            int units = Integer.parseInt(commandLine.split(" ")[1]);
            switch (command) {
                case "forward" -> horizontalPosition += units;
                case "up" -> depthPosition -= units;
                case "down" -> depthPosition += units;
                default -> throw new IllegalArgumentException("Unexpected command");
            }
        }
        return horizontalPosition * depthPosition;
    }

    /**
     * Part 2
     */
    public static int calculateFinalPositionUsingAim(List<String> input) {
        int horizontalPosition = 0;
        int depthPosition = 0;
        int currentAim = 0;

        for (String commandLine : input) {
            String command = commandLine.split(" ")[0];
            int units = Integer.parseInt(commandLine.split(" ")[1]);
            switch (command) {
                case "forward" -> {
                    horizontalPosition += units;
                    depthPosition += currentAim * units;
                }
                case "up" -> currentAim -= units;
                case "down" -> currentAim += units;
                default -> throw new IllegalArgumentException("Unexpected input");
            }
        }
        return horizontalPosition * depthPosition;
    }
}
