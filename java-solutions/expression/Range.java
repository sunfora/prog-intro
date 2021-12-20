package expression;

import java.util.*;

/* Immutable class to store ranges
 *
 * Important Note:
 *
 * Mathematically speaking if a < b [a; a) == [b; b) == [b; a)
 * Because they all are empty sets.
 *
 * However this is not true for these Range objects.
 *
 * In fact if a != b then Range{[a; a)} is not Range{[b; b)}.
 * But if a < b and c < d then Range{[b; a)} is Range{[d; c)}.
 * Also they are empty sets.
 *
 * Also, all other operations that are defined for sets must work.
 * For example if a <= b < c <= d then
 * Range{[a, d)} is subset of Range{[b; c)}
 *
 * So, I think, that Range is:
 *
 * Range{[a; b)} := {{x} : a <= x <= b} U {x : a <= x < b}
 *
 * And Range class is the class representation of this object
 */
public class Range implements Iterable<Integer> {
// #fields:
    public final boolean empty;
    public final int sup;
    public final int inf;
// fields#

// #constructors:
    // Default constructor from infimum and supremum
    public Range(int inf, int sup) {
        if (sup < inf) {
            empty = true;
            this.inf = Integer.MAX_VALUE;
            this.sup = Integer.MIN_VALUE;
        } else {
            empty = false;
            this.inf = inf;
            this.sup = sup;
        }
    }

    // Copy constructor
    public Range(Range range) {
        if (null == range) {
            empty = true;
            inf = Integer.MAX_VALUE;
            sup = Integer.MIN_VALUE;
        } else {
            empty = range.empty;
            inf = range.inf;
            sup = range.sup;
        }
    }
// constructors#

// #public:
    // Returns true if this range fully lies in another
    public boolean isSubsetOf(Range range) {
        if ((null == range) || range.empty) {
            return false;
        }
        return (range.inf <= inf) && (sup <= range.sup);
    }

    // Returns true if this range contains all elements of another
    public boolean isSupersetOf(Range range) {
        if ((null == range) || range.empty) {
            return true;
        }
        return range.isSubsetOf(this);
    }

    // Returns number of elements, the length of the interval
    public int length() {
        if (empty) {
            return 0;
        }
        return sup - inf;
    }

    // Checks whether the number lies in interval
    public boolean contains(int pos) {
        if (empty) {
            return false;
        }
        return ((inf <= pos) && (pos < sup));
    }

    // Returns an array of points in interval
    public int[] toArray() {
        int[] result = new int[length()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = i + inf;
        }
        return result;
    }
// public#

// #implemented:
    @Override
    public String toString() {
        if (empty) {
            return "Range{empty}";
        }
        return "Range{[" + inf + ", " + sup + ")}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Range) {
            Range range = (Range) obj;
            return (range.inf == inf) && (range.sup == sup);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 17*Integer.hashCode(inf) + sup;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new RangeIterator();
    }
// implemented#

    // RangeIterator iterates for every Integer from inf to sup
    private class RangeIterator implements Iterator<Integer> {
        private int current;

        public RangeIterator() {
            current = inf;
        }

        public boolean hasNext() {
            return current < sup;
        }

        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Range iterator reached end");
            }
            return current++;
        }
    }
}