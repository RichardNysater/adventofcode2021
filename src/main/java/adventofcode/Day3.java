package adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day3 {
    private static final char ZERO = '0';
    private static final char ONE = '1';

    public static void main(String[] argv) throws IOException {
        List<String> bitStrings = Files.readAllLines(Path.of("src/main/resources/day3.txt"));

        System.out.println(calculatePowerConsumption(bitStrings));
        System.out.println(calculateLifeSupportRating(bitStrings));
    }

    /**
     * Part 1
     */
    public static Integer calculatePowerConsumption(List<String> bitStrings) {
        List<Map<Character, Integer>> bitCount = calculateBitCount(bitStrings);

        StringBuilder gammaRateBuilder = new StringBuilder();
        StringBuilder epsilonRateBuilder = new StringBuilder();

        for (int i = 0; i < bitStrings.get(0).length(); i++) {
            Integer zeroCount = bitCount.get(i).getOrDefault(ZERO, 0);
            Integer oneCount = bitCount.get(i).getOrDefault(ONE, 0);

            if (zeroCount > oneCount) {
                gammaRateBuilder.append("0");
                epsilonRateBuilder.append("1");
            } else if (oneCount > zeroCount) {
                gammaRateBuilder.append("1");
                epsilonRateBuilder.append("0");
            } else {
                throw new IllegalArgumentException("Unexpected result");
            }
        }

        int gammaRate = convertBitStringToInteger(gammaRateBuilder.toString());
        int epsilonRate = convertBitStringToInteger(epsilonRateBuilder.toString());

        return gammaRate * epsilonRate;
    }

    /**
     * Part 2
     */
    private static int calculateLifeSupportRating(List<String> bitStrings) {
        List<String> keptOxygenLines = new ArrayList<>(bitStrings);
        List<String> keptCo2lines = new ArrayList<>(bitStrings);

        Integer oxygenRating = null;
        Integer co2Rating = null;

        for (int i = 0; i < bitStrings.get(0).length(); i++) {
            List<String> oxygenLinesToKeep = calculateOxygenLinesToKeep(keptOxygenLines, i);

            if (oxygenLinesToKeep.size() == 1) {
                oxygenRating = convertBitStringToInteger(oxygenLinesToKeep.get(0));
                break;
            } else {
                keptOxygenLines = oxygenLinesToKeep;
            }
        }

        for (int i = 0; i < bitStrings.get(0).length(); i++) {
            List<String> co2LinesToKeep = calculateCo2LinesToKeep(keptCo2lines, i);

            if (co2LinesToKeep.size() == 1) {
                co2Rating = convertBitStringToInteger(co2LinesToKeep.get(0));
                break;
            } else {
                keptCo2lines = co2LinesToKeep;
            }
        }

        return oxygenRating * co2Rating;
    }


    private static int convertBitStringToInteger(String bitString) {
        int value = 0;
        int currentValue = 1;
        for (int i = bitString.length() - 1; i >= 0; i--) {
            value += bitString.charAt(i) == ONE ? currentValue : 0;
            currentValue *= 2;
        }
        return value;
    }

    private static List<String> calculateOxygenLinesToKeep(List<String> bitStrings, int curIndex) {
        return calculateLinesToKeep(bitStrings, curIndex, ONE, ZERO);
    }

    private static List<String> calculateCo2LinesToKeep(List<String> bitStrings, int curIndex) {
        return calculateLinesToKeep(bitStrings, curIndex, ZERO, ONE);
    }

    private static List<String> calculateLinesToKeep(List<String> bitStrings, int curIndex, char charToKeepIfLessZeroesThanOnes, char charToKeepIfMoreZeroesThanOnes) {
        List<Map<Character, Integer>> bitCount = calculateBitCount(bitStrings);

        List<String> co2LinesToKeep = new ArrayList<>();
        Integer zeroCount = bitCount.get(curIndex).getOrDefault(ZERO, 0);
        Integer oneCount = bitCount.get(curIndex).getOrDefault(ONE, 0);
        if (oneCount > zeroCount || oneCount.equals(zeroCount)) {
            for (var line : bitStrings) {
                if (line.charAt(curIndex) == charToKeepIfLessZeroesThanOnes) {
                    co2LinesToKeep.add(line);
                }
            }
        } else {
            for (var line : bitStrings) {
                if (line.charAt(curIndex) == charToKeepIfMoreZeroesThanOnes) {
                    co2LinesToKeep.add(line);
                }
            }
        }
        return co2LinesToKeep;
    }

    private static List<Map<Character, Integer>> calculateBitCount(List<String> bitStrings) {
        int characterCount = bitStrings.get(0).length();

        List<Map<Character, Integer>> bitCount = new ArrayList<>();
        for (int i = 0; i < characterCount; i++) {
            bitCount.add(new HashMap<>());
        }

        for (int i = 0; i < characterCount; i++) {
            for (String line : bitStrings) {
                char curChar = line.charAt(i);
                Integer currentCount = bitCount.get(i).getOrDefault(curChar, 0);
                bitCount.get(i).put(curChar, currentCount + 1);
            }
        }
        return bitCount;
    }
}
