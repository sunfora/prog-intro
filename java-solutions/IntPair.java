import java.util.*;

public class IntPair implements Comparable<IntPair> {
    public int first;
    public int second;

    public IntPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public int compareTo(IntPair other) {
        int firstCmp = Integer.compare(this.first, other.first);
        if (firstCmp < 0) {
            return -1;
        } else if (firstCmp > 0) {
            return 1;
        }
        return Integer.compare(this.second, other.second);
    }
}