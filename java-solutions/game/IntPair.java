package game;

import java.util.Objects;
import java.util.List;

/**
 * Class for int pairs
 */
public final class IntPair {
// #fields:
    public final int x;
    public final int y;
// fields#

// #constructors:
    public IntPair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public IntPair(int[] arr) {
        Objects.requireNonNull(arr);
        if (arr.length == 2) {
            this.x = arr[0];
            this.y = arr[1];
        } else {
            throw new IllegalArgumentException("Expected array of int with 2 integers");
        }
    }

    public IntPair(List<Integer> list) {
        Objects.requireNonNull(list);
        if (list.size() == 2) {
            this.x = list.get(0);
            this.y = list.get(1);
        } else {
            throw new IllegalArgumentException("Expected list with 2 integers");
        }
    }
// constructors#

// #public:
    public IntPair add(IntPair other) {
        return new IntPair(other.x + x, other.y + y);
    }

    public IntPair sub(IntPair other) {
        return new IntPair(x - other.x, y - other.y);
    }

    public IntPair times(int m) {
        return new IntPair(x * m, y * m);
    }
// public#

// #implemeted:
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntPair) {
            return (((IntPair)obj).x == x) && (((IntPair)obj).y == y);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 17*Integer.hashCode(x) + y;
    }

    public String toString() {
        return String.format("IntPair(%d, %d)", x, y);
    }
// implemented#
}