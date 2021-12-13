package md2html;

import java.util.*;

public class TestSolutionFinder {
    private static int cnt = 0;

    public static void main(String[] args) {
        test(13, new IntList(2, 5, 10), new IntList(10, 2));
        test(12, new IntList(2, 5, 10), new IntList(10, 2));
        test(22, new IntList(2, 5, 10), new IntList(10, 10, 2));
        test(96, new IntList(81, 4, 11, 9, 1), new IntList(11, 81, 4));
        fail(115, new IntList(-1, 14), new IntList());
        fail(124412, new IntList(-1), new IntList());
    }

    private static void fail(int value, IntList seq, IntList answer) {
        boolean passed = true;
        try {
        } catch (IllegalArgumentException e) {
            passed = false;
        }
        assert !passed : "Test passed:" + value + " " + seq + " "+ answer;
    }

    private static void test(int value, IntList seq, IntList answer) {
        System.out.println("Running test: " + cnt++);
        IntList result = new IntList(new SolutionFinder(List.of(seq.toIntegerArray())).find(value));
        assert result.equals(answer) : "Expected: " + answer.toString(" ") + System.lineSeparator()
                                     + "Found: " + result.toString(" ");
    }
}