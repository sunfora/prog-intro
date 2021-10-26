package soup.util;

/* Immutable class to store ranges
 *
 * Important Note:
 *
 * Mathematically speaking if a < b [a; a) == [b; b) == [b; a)
 * Because they all are empty sets.
 *
 * However this is not true for Range objects.
 *
 * In fact if a != b then Range{[a; a)} is not Range{[b; b)}.
 * But if a < b and c < d then Range{[b; a)} is Range{[d; c)}.
 * Also they are empty sets.
 *
 * Also, all other operations that are defined for sets must work.
 * For example if a <= b < c <= d then
 * Range{[a, d)} is subset of Range{[b; c)}
 *
 * So, I think, that mathematical construction of Range object should be the following:
 *
 * Range{[a; b)} := {{x} : a <= x <= b} U {x : a <= x < b}
 *
 * And Range class is the class representation of this object
 */
public class Range {

    public final boolean empty;
    public final int sup;
    public final int inf;

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

    public Range(Range range) {
        if (null == range) {
            empty = true;
            inf = Integer.MAX_VALUE;
            sup = Integer.MIN_VALUE;
        } else {
            empty = false;
            inf = range.inf;
            sup = range.sup;
        }
    }

    public boolean isSubsetOf(Range range) {
        if ((null == range) || range.empty) {
            return false;
        }
        return (range.inf <= inf) && (sup <= range.sup);
    }

    public boolean isSupersetOf(Range range) {
        if ((null == range) || range.empty) {
            return true;
        }
        return range.isSubsetOf(this);
    }

    public int length() {
        if (empty) {
            return 0;
        }
        return sup - inf;
    }

    public boolean contains(int pos) {
        if (empty) {
            return false;
        }
        return ((inf <= pos) && (pos < sup));
    }

    @Override
    public String toString() {
        if (empty) {
            return "Range{empty}";
        }
        return "Range{[" + inf + ", " + sup + ")}";
    }
}