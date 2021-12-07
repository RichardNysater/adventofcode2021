package adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day1 {

    public static void main(String[] argv) throws IOException {
        List<Integer> lines = Files.readAllLines(Path.of("src/main/resources/day1.txt")).stream().map(Integer::parseInt).toList();

        System.out.println(getTotalDepthMeasurement(lines, 1));
        System.out.println(getTotalDepthMeasurement(lines, 3));
    }

    public static int getTotalDepthMeasurement(List<Integer> measurements, Integer slidingWindowSize) {
        Integer previousSlidingWindow = null;
        int totalIncreases = 0;

        Queue<Integer> queue = new LinkedList<>();

        for (var measurement : measurements) {
            if (isSlidingWindowPopulated(slidingWindowSize, queue)) {
                Integer ejectedMeasurement = queue.poll();
                queue.add(measurement);

                int newSlidingWindow = previousSlidingWindow - ejectedMeasurement + measurement;

                if (newSlidingWindow > previousSlidingWindow) {
                    totalIncreases++;
                }

                previousSlidingWindow = newSlidingWindow;
            } else {  // Populate measurement window
                queue.add(measurement);

                if (previousSlidingWindow == null) {
                    previousSlidingWindow = measurement;
                } else {
                    previousSlidingWindow += measurement;
                }
            }
        }

        return totalIncreases;
    }

    private static boolean isSlidingWindowPopulated(Integer slidingWindowSize, Queue<Integer> queue) {
        return queue.size() == slidingWindowSize;
    }

}
