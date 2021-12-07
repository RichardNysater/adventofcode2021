package adventofcode;

import adventofcode.data.Pair;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day6 {
    private static final Integer TIME_UNTIL_SPAWNING_FOR_NEW_FISH = 8;
    private static final Integer TIME_UNTIL_SPAWNING_FOR_EXISTING_FISH = 6;

    private static final Map<Pair<Integer, Integer>, BigInteger> totalFishesPerDay = new HashMap<>();

    public static void main(String[] argv) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/day6.txt"));

        System.out.println(calculateFishes(lines.get(0), 80));
        System.out.println(calculateFishesTwo(lines.get(0), 256));
    }

    public static int calculateFishes(String strFish, Integer daysInFuture) {
        List<LanternFish> startFishes = Arrays.stream(strFish.split(",")).map(timeUntilSpawningStr -> new LanternFish(Integer.parseInt(timeUntilSpawningStr))).toList();

        List<LanternFish> fishes = new ArrayList<>(startFishes);
        for (int i = 0; i < daysInFuture; i++) {
            int newFishes = 0;
            for (var fish : fishes) {
                if (fish.passTime()) {
                    newFishes++;
                }
            }
            for (int j = 0; j < newFishes; j++) {
                fishes.add(new LanternFish(TIME_UNTIL_SPAWNING_FOR_NEW_FISH));
            }
        }
        return fishes.size();
    }

    public static String calculateFishesTwo(String strFish, Integer daysInFuture) {
        List<LanternFish> startFishes = Arrays.stream(strFish.split(",")).map(timeUntilSpawningStr -> new LanternFish(Integer.parseInt(timeUntilSpawningStr))).toList();

        List<LanternFish> fishes = new ArrayList<>(startFishes);
        BigInteger totalFishes = BigInteger.valueOf(fishes.size());
        for (var fish : fishes) {
            totalFishes = totalFishes.add(passTime(daysInFuture, fish.timeUntilSpawning));
        }
        return totalFishes.toString();
    }

    public static BigInteger passTime(int days, int timeUntilSpawning) {
        if (timeUntilSpawning >= days) {
            return BigInteger.ZERO;
        }
        Pair<Integer, Integer> pair = new Pair<>(days, timeUntilSpawning);
        if (totalFishesPerDay.containsKey(pair)) {
            return totalFishesPerDay.get(pair);
        }

        int daysLeftAfterFirstSpawn = days - (1 + timeUntilSpawning);

        BigInteger result = BigInteger.ONE.add(passTime(daysLeftAfterFirstSpawn, TIME_UNTIL_SPAWNING_FOR_EXISTING_FISH)).add(passTime(daysLeftAfterFirstSpawn, TIME_UNTIL_SPAWNING_FOR_NEW_FISH));
        totalFishesPerDay.put(pair, result);
        return result;
    }


    public static class LanternFish {
        private Integer timeUntilSpawning;

        public LanternFish(Integer timeUntilSpawning) {
            this.timeUntilSpawning = timeUntilSpawning;
        }

        public boolean passTime() {
            timeUntilSpawning--;
            if (timeUntilSpawning < 0) {
                timeUntilSpawning = 6;
                return true;
            }
            return false;
        }


    }
}
