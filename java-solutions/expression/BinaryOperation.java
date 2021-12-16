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
        this.min1 = min1;
        this.min2 = min2;
        left = -min1.getPriority() < -getPriority();
        right = -getPriority() > -min2.getPriority();

        if (min2 instanceof Operation) {
            Operation op2 = (Operation) min2;
            if (op2.getPriority() == getPriority()) {
                right = !rightAssociative() || !op2.leftAssociative();
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
    public void fastToString(StringBuilder sb) {
        sb.append('(');
        min1.fastToString(sb);
        sb.append(" " + getOperation() + " ");
        min2.fastToString(sb);
        sb.append(')');
    }

    @Override
    public void fastToMiniString(StringBuilder sb) {
        fastBrackets(left, sb, min1);
        sb.append(" " + getOperation() + " ");
        fastBrackets(right, sb, min2);
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