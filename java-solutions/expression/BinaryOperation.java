package expression;

import java.util.Objects;
import java.math.BigInteger;

public abstract class BinaryOperation extends Operation {

    protected final PolyExpression min1;
    protected final PolyExpression min2;

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
            int p1 = -((Operation) min2).getPriority();
            int p2 = -getPriority();

            right = p1 < p2; //default

            if (min2.getClass() == this.getClass()) {
                right = !selfAssociative();
            } else if (p1 == p2) {
                right = !rightAssociative() || !((Operation) min2).leftAssociative();
            }
        }
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

    @Override
    public int hashCode() {
        return (min1.hashCode() * 17 * 17) + (min2.hashCode() * 17) + this.getClass().hashCode();

    }

    public PolyExpression getLeft() {
        return min1;
    }

    public PolyExpression getRight() {
        return min2;
    }
}