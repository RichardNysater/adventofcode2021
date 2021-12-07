package adventofcode;

import adventofcode.data.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day4 {

    public static void main(String[] argv) throws IOException {
        List<String> lines = Files.readAllLines(Path.of("src/main/resources/day4.txt"));

        List<Integer> drawnNumbers = Arrays.stream(lines.get(0).split(",")).map(Integer::parseInt).toList();

        List<Board> boards = new ArrayList<>();
        for (int cur = 2; cur < lines.size() - 2; cur += 6) {
            List<String> boardLines = new ArrayList<>();
            boardLines.add(lines.get(cur));
            boardLines.add(lines.get(cur + 1));
            boardLines.add(lines.get(cur + 2));
            boardLines.add(lines.get(cur + 3));
            boardLines.add(lines.get(cur + 4));
            boards.add(new Board((cur - 2) / 6, boardLines));
        }

        System.out.println(calculateBingoWinner(boards, drawnNumbers));
        System.out.println(calculateBingoLoser(boards, drawnNumbers));

    }

    /**
     * Part 1
     */
    private static int calculateBingoWinner(List<Board> boards, List<Integer> drawnNumbers) {
        for (var number : drawnNumbers) {
            List<Board> winners = drawNumber(boards, number);
            if (!winners.isEmpty()) {
                System.out.println("Winner winner chicken dinner: " + winners + " for number " + number);
                return number * winners.stream().map(Board::calculateSumOfUndrawnNumbers).reduce(Integer::max).orElseThrow();
            }
        }
        throw new IllegalArgumentException("No winners found");
    }

    /**
     * Part 2
     */
    private static int calculateBingoLoser(List<Board> boards, List<Integer> drawnNumbers) {
        final Set<Integer> wonBoardIndices = new HashSet<>();
        for (var number : drawnNumbers) {
            List<Board> winners = drawNumber(boards, number);

            Set<Integer> newWonBoardIndices = new HashSet<>(wonBoardIndices);
            winners.forEach(board -> newWonBoardIndices.add(board.index));

            if (newWonBoardIndices.size() == boards.size()) {
                Board loser = winners.stream().filter(board -> !wonBoardIndices.contains(board.index)).findAny().orElseThrow();
                System.out.println("Loser Loser chicken snoozer: " + loser + " for number " + number);
                return number * loser.calculateSumOfUndrawnNumbers();
            }
            wonBoardIndices.addAll(newWonBoardIndices);
        }
        throw new IllegalArgumentException("No winners found");
    }

    private static List<Board> drawNumber(List<Board> boards, int number) {
        List<Board> winningBoards = new ArrayList<>();
        for (var board : boards) {
            boolean win = board.markNumber(number);
            if (win) {
                winningBoards.add(board);
            }
        }
        return winningBoards;
    }

    private static class Board {
        List<List<Pair<Integer, Boolean>>> board;
        Integer index;

        public Board(Integer index, List<String> lines) {
            this.index = index;
            if (lines.size() != 5) {
                throw new IllegalArgumentException("Invalid board");
            }
            board = new ArrayList<>(5);

            for (String line : lines) {
                ArrayList<Pair<Integer, Boolean>> row = new ArrayList<>();
                String[] splitLine = line.split("\\s+");
                for (var j : splitLine) {
                    if (!j.equals("")) {
                        row.add(new Pair<>(Integer.parseInt(j), false));
                    }
                }
                board.add(row);
            }
        }

        public boolean isWinner() {
            return hasRowWin() || hasColumnWin() || hasRightDiagonalWin() || hasLeftDiagonalWin();
        }

        private boolean hasRowWin() {
            for (var row : board) {
                boolean isWin = true;
                for (var value : row) {
                    if (!value.getSecond()) {
                        isWin = false;
                        break;
                    }
                }
                if (isWin) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasColumnWin() {
            for (int column = 0; column < board.size(); column++) {
                boolean isWin = true;

                for (List<Pair<Integer, Boolean>> row : board) {
                    if (!row.get(column).getSecond()) {
                        isWin = false;
                        break;
                    }
                }
                if (isWin) {
                    return true;
                }
            }
            return false;
        }

        private boolean hasRightDiagonalWin() {
            boolean isWin = true;
            for (int i = 0; i < board.size(); i++) {
                if (!board.get(i).get(i).getSecond()) {
                    isWin = false;
                    break;
                }
            }
            return isWin;
        }

        private boolean hasLeftDiagonalWin() {
            boolean isWin = true;
            for (int i = 0; i < board.size(); i++) {
                if (!board.get(i).get(board.size() - 1 - i).getSecond()) {
                    isWin = false;
                    break;
                }
            }
            return isWin;
        }

        public int calculateSumOfUndrawnNumbers() {
            int sum = 0;
            for (var row : board) {
                for (var value : row) {
                    if (!value.getSecond()) {
                        sum += value.getFirst();
                    }
                }
            }
            return sum;
        }

        public boolean markNumber(int number) {
            boolean isNumberMarked = markNumberWithoutCheckingWin(number);
            return isNumberMarked && isWinner();
        }

        private boolean markNumberWithoutCheckingWin(int number) {
            for (var row : board) {
                for (int i = 0; i < row.size(); i++) {
                    if (row.get(i).getFirst().equals(number)) {
                        row.set(i, new Pair<>(number, true));
                        return true;
                    }
                }
            }
            return false;
        }

        public String toString() {
            StringBuilder output = new StringBuilder();
            for (var row : board) {
                output.append(row).append("\n");
            }
            return output.toString();
        }
    }
}
