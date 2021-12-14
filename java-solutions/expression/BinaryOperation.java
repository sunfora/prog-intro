package expression;

import java.util.Objects;
import java.math.BigInteger;

public abstract class BinaryOperation extends Operation {

    protected PolyExpression min1;
    protected PolyExpression min2;

    protected boolean left;
    protected boolean right;

    protected abstract int apply(int x, int y);
    protected abstract BigInteger apply(BigInteger x, BigInteger y);

    public BinaryOperation(PolyExpression min1, PolyExpression min2) {
        this.min1 = Objects.requireNonNull(min1);
        this.min2 = Objects.requireNonNull(min2);

        if (min1 instanceof Operation) {
            left = -((Operation) min1).getPriority() < -getPriority();
        }

        if (min2 instanceof Operation) {
            right = -((Operation) min2).getPriority() <= -getPriority();
        }

        if (rightAssociativeWith(min2)) {
            right = false;
        }
    }

    protected boolean rightAssociativeWith(PolyExpression min) {
        return false;
    }

    @Override
    public int evaluate(int x) {
        return apply(min1.evaluate(x), min2.evaluate(x));
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return apply(
            min1.evaluate(x, y, z),
            min2.evaluate(x, y, z)
        );
    }

    @Override
    public BigInteger evaluate(BigInteger x) {
        return apply(
            min1.evaluate(x),
            min2.evaluate(x)
        );
    }

    @Override
    protected void fastToString(StringBuilder sb) {
        sb.append('(');
        fast(false, min1, sb);
        sb.append(' ').append(getOperation()).append(' ');
        fast(false, min2, sb);
        sb.append(')');
    }

    @Override
    public void fastToMiniString(StringBuilder sb) {
        fastBrackets(left, true, sb, min1);
        sb.append(' ').append(getOperation()).append(' ');
        fastBrackets(right, true, sb, min2);
    }

    @Override
    public boolean equals(final Object other) {
        if (null == other) {
            return false;
        }
        if (this.getClass() == other.getClass()) {
            BinaryOperation otherM = (BinaryOperation) other;
            return (min1.equals(otherM.min1)) && (min2.equals(otherM.min2));
        }
        return false;
    }

    protected boolean hashCached;
    protected int     hashCache;

    @Override
    public int hashCode() {
        if (!hashCached) {
            hashCached = true;
            return hashCache = (min1.hashCode() * 17 * 17)
                                + (min2.hashCode() * 17)
                                + this.getClass().hashCode();
        }
        return hashCache;
    }

    public PolyExpression getLeft() {
        return min1;
    }

    public PolyExpression getRight() {
        return min2;
    }
}