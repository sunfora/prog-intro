import java.util.Arrays;
import java.io.StringReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.NoSuchElementException;

public class Reverse {
    public static void main(String[] args) {
        try {
            int[][] matrix = scanLines();
            printReversed(matrix);
        } catch (IOException e) {
            System.out.println("IOException occured " + e.getMessage());
        }
    }

    public static int[][] scanLines() throws IOException {
        int cnt = 0;
        int[][] lines = new int[1][];
        int[] buffer = new int[1];
        Split split = new Split(
            new InputStreamReader(System.in, "utf8"),
            new NewLine(),
            new Whitespace()
        );
        Split.View byLines = split.view(1);
        HexDecFilter nums = new HexDecFilter(split.view(2));
	int sub = 0;
        try {
            while (byLines.hasNext()) {
		System.err.println(byLines.showToken());
                int cntNums = 0;
                while (nums.hasNext()) {
                    if (cntNums >= buffer.length) {
                        buffer = Arrays.copyOf(buffer, buffer.length * 2);
                    }
                    buffer[cntNums++] = nums.next();
                }
                if (cnt >= lines.length) {
                    lines = Arrays.copyOf(lines, lines.length * 2);
                }
                lines[cnt++] = Arrays.copyOf(buffer, cntNums);
                byLines.next();
		sub = cntNums > 0? 0 : 1;
            }
        } finally {
            split.close();
        }
        return Arrays.copyOf(lines, cnt - sub);
    }


    public static void printReversed(int[][] parsed) {
        for (int i = parsed.length - 1; i >= 0; i--) {
            printIntArray(parsed[i], parsed[i].length - 1, -1, -1);
        }
    }

    /**
     * Prints values of array on the range of indexes
     * If step > 0 then if start < end it prints ints[start + step*k] k = 0, 1...
     * If step < 0 then if start > end it prints ints[start + step*k] k = 0, 1...
     * In any other case it does nothing and always prints a new line
     */
    public static void printIntArray(int[] ints, int start, int end, int step) {
        int m = 0;
        if ((step > 0) && (start < end)) {
            m = 1;
        } else if ((step < 0) && (start > end)) {
            m = -1;
        }
        int j = start;
        while (m*j < m*end) {
            if (j != start) {
		System.err.print(" ");
                System.out.print(" ");
            }
	    System.err.print(ints[j]);
            System.out.print(ints[j]);
            j += step;
        }
	System.err.print(System.lineSeparator());
        System.out.print(System.lineSeparator());
    }

    public static void printIntArray(int[] ints, int start, int end) {
        printIntArray(ints, start, end, 1);
    }

    public static void printIntArray(int[] ints, int end) {
        printIntArray(ints, 0, end);
    }

    public static void printIntArray(int[] ints) {
        printIntArray(ints, ints.length);
    }
}