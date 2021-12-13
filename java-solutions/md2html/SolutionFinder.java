package md2html;

import java.util.*;

/*
 * Problem:
 *     Given a sequence of positive integer numbers > a_1, a_2, a_3, a_4 ... a_n
 *     for any given number t, find exact or nearest solution sequence b:
 *         b_1, b_2, b_3 ... b_k, such that sum(b_j) <= t, and sum(b_j) is maximum.
 * Further restrictions:
 *     1. k should be minimal
 *     2. for valid b-sequence of length k, sequence with the highest term in a is chosen
 * This class constructs solutions on the fly. Meaning Iterable<Integer> is returned.
 *
 * To store info list of int's is used. SolutionFinder requires O(t) space.
 */

public class SolutionFinder {
// #consts: /*fold00*/
    private final static int END = 0;
// consts# /*FOLD00*/

// fields: /*FOLD00*/
    private final IntList sequence;
    private final IntList nearest;
    private final IntList paths;
    private final IntList solution;
// fields# /*FOLD00*/

// #constructors: /*FOLD00*/
    public SolutionFinder(Collection<Integer> seq) { /*FOLD01*/
        sequence = new IntList(seq);
        int size = 0;
        for (int i = 0; i < sequence.size(); ++i) {
            assert sequence.get(i) > 0 : "Found not a positive value in input :" + sequence.get(i);
            size = Math.max(size, sequence.get(i));
        }
        size += 1;
        nearest = new IntList(0);
        solution = new IntList(0);
        paths = new IntList(0);
        sequence.append(0);
        int j = 0;
        for (int i = 1; i < size; ++i) {
            nearest.append(nearest.get(-1));
            solution.append(0);
            paths.append(0);
            if (i == sequence.get(j)) {
                setSolution(i, sequence.get(j++));
            }
        }
        sequence.reverse();
    } /*FOLD01*/
// constructors# /*FOLD00*/

// #public /*FOLD00*/
    public SolutionIterator find(int t) { /*FOLD01*/
        System.err.println("solution finder" + sequence + " for : " + t);
        if (t >= solution.size()) {
            int old = solution.size();
            solution.resize(t + 1);
            paths.resize(t + 1);
            for (int num = old; num <= t; ++num) {
                nearest.append(nearest.get(num - 1));
                int minSolution = findMinSolution(num);
                if (minSolution != 0) {
                    setSolution(num, minSolution);
                }
            }
        }
        return new SolutionIterator(t);
    } /*FOLD01*/

    public SolutionFinder resizeStorage(int j) { /*FOLD01*/
        if (j >= solution.size()) {
            find(j);
        }
        nearest.resize(j + 1);
        paths.resize(j + 1);
        solution.resize(j + 1);
        return this;
    } /*FOLD01*/
// public# /*FOLD00*/

// #private: /*FOLD00*/
    private boolean exist(int number) {
        return number == nearest.get(number);
    }

    private void setSolution(int i, int solution) { /*FOLD01*/
        this.solution.set(i, solution);
        paths.set(i, paths.get(i - solution) + 1);
        nearest.set(i, i);
    } /*FOLD01*/

    private int findMinSolution(int number) { /*FOLD01*/
        int k = Integer.MAX_VALUE;
        int j = 0;
        for (int i = 1; i < sequence.size(); i++) {
            if (exist(number - sequence.get(i))) {
                if (k > paths.get(number - sequence.get(i))) {
                    k = paths.get(number - sequence.get(i));
                    j = i;
                }
            }
        }
        return sequence.get(j);
    } /*FOLD01*/
// private# /*FOLD00*/

// #nested: /*FOLD00*/
    public class SolutionIterator implements Solution {
    // #fields: /*FOLD01*/
        private int current = END;
        private int sum;
        private final int number;
    // fields# /*FOLD01*/

    // #constructors: /*FOLD01*/
        public SolutionIterator(int number) {
            this.number = number;
            if (number <= 0) {
                return;
            } else if (number >= solution.size()) {
                throw new IllegalStateException(
                    "SolutionIterator was called before solution was found for number: " + number
                );
            }
            current = nearest.get(number);
        }
    // constructors# /*FOLD01*/

    // #public: /*FOLD01*/
        public boolean hasNext() {
            return current != END;
        }

        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more solutions for number: " + number);
            }
            return solution.get(Util.swap(current, current -= solution.get(current)));
        }

        public int sum() {
            return current;
        }

        public Iterator<Integer> iterator() {
            return this;
        }
    // public# /*FOLD01*/
    }
// nested# /*FOLD00*/
}