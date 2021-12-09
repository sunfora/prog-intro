import java.util.Scanner;
import java.util.Arrays;

public class ReverseMin2 {
    public static void main(String[] args) {
        int[][] matrix = scanLines();
        printMinMatrix(matrix);
    }

    public static int[][] scanLines() {
        Scanner input = new Scanner(System.in);
        int cnt = 0;
        int[][] lines = new int[1][];
        int[][] wrappedBuffer = new int[1][1];
        while (input.hasNextLine()) {
            String rawLine = input.nextLine();
            if (cnt >= lines.length) {
                lines = Arrays.copyOf(lines, lines.length * 2);
            }
            lines[cnt++] = parseLine(rawLine, wrappedBuffer);
        }
        input.close();
        return Arrays.copyOf(lines, cnt);
    }

    public static int[] parseLine(String rawLine, int[][] wrappedBuffer) {
        int[] buffer = wrappedBuffer[0];
        Scanner intReader = new Scanner(rawLine);
        int cnt = 0;
        while (intReader.hasNextInt()) {
            if (cnt >= buffer.length) {
                wrappedBuffer[0] = Arrays.copyOf(buffer, buffer.length * 2);
                buffer = wrappedBuffer[0];
            }
            buffer[cnt++] = intReader.nextInt();
        }
        intReader.close();
        return Arrays.copyOf(buffer, cnt);
    }

    public static void printMinMatrix(int[][] parsed) {
        int neutral = Integer.MAX_VALUE;
        int[] acum = {neutral};
        int cnt;
        int offset;
        for (int[] ints : parsed) {
            cnt = ints.length;
            if (acum.length < cnt) {
                offset = acum.length;
                acum = Arrays.copyOf(acum, cnt);
                Arrays.fill(acum, offset, cnt, neutral);
            }
            if (cnt != 0) {
                acum[0] = Math.min(acum[0], ints[0]);
            }
            for (int j = 1; j < cnt; j++) {
                acum[j] = Math.min(acum[j], ints[j]);
                acum[j] = Math.min(acum[j], acum[j - 1]);
            }
            printIntArray(acum, cnt);
        }
    }

    /**
    * Prints values of array on the range of indexes
    * If step = 0 then if start != end it prints ints[start]
    * If step > 0 then if start < end it prints ints[start + step*k] k = 0, 1...
    * If step < 0 then if start > end it prints ints[start + step*k] k = 0, 1...
    * In any other case it does nothing and always prints a new line
    */
    public static void printIntArray(int[] ints, int start, int end, int step) {
        if (step == 0 && start != end) {
            System.out.print(ints[start]);
        }
        boolean caseStepPositive = (step > 0) && (start < end);
        boolean caseStepNegative = (step < 0) && (start > end);
        if (caseStepPositive || caseStepNegative) {
            int j = start;
            while ((caseStepPositive)? j < end : j > end) {
                if (j != start) {
                    System.out.print(" ");
                }
                System.out.print(ints[j]);
                j += step;
            }
        }
        System.out.println();
    }

    public static void printIntArray(int[] ints, int start, int step) {
        printIntArray(ints, start, step, 1);
    }

    public static void printIntArray(int[] ints, int end) {
        printIntArray(ints, 0, end);
    }

    public static void printIntArray(int[] ints) {
        printIntArray(ints, ints.length);
    }
}