import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImprovedGridCipher {
    private final String key;
    private final int rows;
    private final int cols;

    public ImprovedGridCipher(String key, int rows, int cols) {
        this.key = prepareKey(key);
        this.rows = rows;
        this.cols = cols;
    }

    private String prepareKey(String key) {
        String cleaned = key.replaceAll("[^a-zA-Z]", "").toUpperCase();

        Set<Character> seen = new HashSet<>();
        StringBuilder uniqueKey = new StringBuilder();

        for (char c : cleaned.toCharArray()) {
            if (!seen.contains(c)) {
                seen.add(c);
                uniqueKey.append(c);
            }
        }

        for (char c = 'Z'; c >= 'A'; c--) {
            if (!seen.contains(c)) {
                uniqueKey.append(c);
            }
        }

        return uniqueKey.toString();
    }

    public String encrypt(String message) {
        String cleaned = message.replaceAll("[^a-zA-Z]", "").toUpperCase();
        char[][] grid = new char[rows][cols];
        fillGridSpiral(grid, cleaned);

        return readGridByKey(grid);
    }

    private void fillGridSpiral(char[][] grid, String message) {
        int top = 0, bottom = rows - 1, left = 0, right = cols - 1;
        int index = 0;

        while (true) {
            if (left > right) break;

            for (int i = left; i <= right; i++) {
                if (index < message.length()) {
                    grid[top][i] = message.charAt(index++);
                } else {
                    grid[top][i] = 'X';
                }
            }
            top++;

            if (top > bottom) break;

            // Правый столбец (сверху вниз)
            for (int i = top; i <= bottom; i++) {
                if (index < message.length()) {
                    grid[i][right] = message.charAt(index++);
                } else {
                    grid[i][right] = 'X';
                }
            }
            right--;

            if (left > right) break;

            for (int i = right; i >= left; i--) {
                if (index < message.length()) {
                    grid[bottom][i] = message.charAt(index++);
                } else {
                    grid[bottom][i] = 'X';
                }
            }
            bottom--;

            if (top > bottom) break;

            for (int i = bottom; i >= top; i--) {
                if (index < message.length()) {
                    grid[i][left] = message.charAt(index++);
                } else {
                    grid[i][left] = 'X';
                }
            }
            left++;
        }
    }

    private String readGridByKey(char[][] grid) {
        StringBuilder result = new StringBuilder();

        List<int[]> readingOrder = getReadingOrder();

        for (int[] pos : readingOrder) {
            int row = pos[0];
            int col = pos[1];
            result.append(grid[row][col]);
        }

        return result.toString();
    }

    private List<int[]> getReadingOrder() {
        List<int[]> order = new ArrayList<>();

        for (int d = 0; d < rows + cols - 1; d++) {
            int row = Math.max(0, d - cols + 1);
            int col = Math.min(d, cols - 1);

            while (row < rows && col >= 0) {
                order.add(new int[]{row, col});
                row++;
                col--;
            }
        }

        return order;
    }

    public String decrypt(String ciphertext) {
        char[][] grid = new char[rows][cols];

        fillGridForDecryption(grid, ciphertext);

        return extractSpiralMessage(grid);
    }

    private void fillGridForDecryption(char[][] grid, String ciphertext) {
        List<int[]> readingOrder = getReadingOrder();
        int index = 0;

        for (int[] pos : readingOrder) {
            if (index < ciphertext.length()) {
                grid[pos[0]][pos[1]] = ciphertext.charAt(index++);
            } else {
                grid[pos[0]][pos[1]] = 'X';
            }
        }
    }

    private String extractSpiralMessage(char[][] grid) {
        StringBuilder message = new StringBuilder();
        int top = 0, bottom = rows - 1, left = 0, right = cols - 1;

        while (true) {
            if (left > right) break;

            for (int i = left; i <= right; i++) {
                message.append(grid[top][i]);
            }
            top++;

            if (top > bottom) break;

            for (int i = top; i <= bottom; i++) {
                message.append(grid[i][right]);
            }
            right--;

            if (left > right) break;

            for (int i = right; i >= left; i--) {
                message.append(grid[bottom][i]);
            }
            bottom--;

            if (top > bottom) break;

            for (int i = bottom; i >= top; i--) {
                message.append(grid[i][left]);
            }
            left++;
        }

        return message.toString().replaceAll("X+$", "");
    }
}
