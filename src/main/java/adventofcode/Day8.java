package adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Day8 {
    private static final Map<Integer, String> SEGMENTS_BY_KNOWN_SIGNAL_COUNT = Map.of(2, "cf",
            4, "bcdf",
            3, "acf",
            7, "abcdefg");

    public static void main(String[] argv) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/day8.txt"));

        System.out.println(countKnownSignals(lines));
    }

    public static long countKnownSignals(List<String> lines) {
        return lines.stream()
                .map(line -> line.split(" \\| ")[1].split(" "))
                .flatMap(Stream::of)
                .map(String::length)
                .filter(SEGMENTS_BY_KNOWN_SIGNAL_COUNT::containsKey)
                .count();
    }
}
