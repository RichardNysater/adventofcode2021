package adventofcode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Day10 {
    private static final Set<Character> OPENING_CHARACTERS = Set.of('(', '[', '<', '{');
    private static final Set<Character> CLOSING_CHARACTERS = Set.of(')', ']', '>', '}');
    private static final Map<Character, Integer> SCORE_BY_ILLEGAL_CHARACTER = Map.of(
            ')', 3,
            ']', 57,
            '}', 1197,
            '>', 25137);

    private static final Map<Character, Integer> SCORE_BY_CLOSING_CHARACTER = Map.of(
            ')', 1,
            ']', 2,
            '}', 3,
            '>', 4);

    private static final Map<Character, Character> OPENING_CHARACTER_BY_CLOSING_CHARACTER = Map.of(
            ')', '(',
            ']', '[',
            '}', '{',
            '>', '<');


    private static final Map<Character, Character> CLOSING_CHARACTER_BY_OPENING_CHARACTER = Map.of(
            '(', ')',
            '[', ']',
            '{', '}',
            '<', '>');
    public static final int SCORE_MULTIPLIER = 5;

    public static void main(String[] argv) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/day10.txt"));
        System.out.println(calculateSyntaxErrorScore(lines));
        System.out.println(calculateCompletionScore(lines));
    }

    private static long calculateSyntaxErrorScore(List<String> lines) {
        return lines.stream().map(Day10::calculateSyntaxErrorScore).reduce(Long::sum).orElseThrow();
    }

    private static long calculateCompletionScore(List<String> lines) {
        List<String> uncorruptedLines = lines.stream().filter(line -> calculateSyntaxErrorScore(line) == 0).toList();

        List<Long> scores = uncorruptedLines.stream().map(Day10::calculateCompletionScore).sorted().toList();
        return scores.get(scores.size() / 2);
    }

    private static long calculateCompletionScore(String line) {
        Stack<Character> stack = new Stack<>();

        for (Character c : line.toCharArray()) {
            if (OPENING_CHARACTERS.contains(c)) {
                stack.push(c);
            } else if (CLOSING_CHARACTERS.contains(c)) {
                stack.pop();
            }
        }

        long score = 0;
        while (stack.size() > 0) {
            Character c = stack.pop();

            score *= SCORE_MULTIPLIER;
            score += SCORE_BY_CLOSING_CHARACTER.get(CLOSING_CHARACTER_BY_OPENING_CHARACTER.get(c));
        }

        return score;
    }

    private static long calculateSyntaxErrorScore(String line) {
        Stack<Character> stack = new Stack<>();

        for (Character c : line.toCharArray()) {
            if (OPENING_CHARACTERS.contains(c)) {
                stack.push(c);
            } else if (CLOSING_CHARACTERS.contains(c)) {
                Character openingChar = stack.pop();
                if (!OPENING_CHARACTER_BY_CLOSING_CHARACTER.get(c).equals(openingChar)) {
                    return SCORE_BY_ILLEGAL_CHARACTER.get(c);
                }
            }
        }
        return 0;
    }

}
