package expression;

import java.math.BigInteger;

public abstract class UnaryOperation extends Operation {

    protected boolean enclose;
    protected boolean left;
    protected PolyExpression min;

    public UnaryOperation(PolyExpression min, boolean left) {
        if (min instanceof Operation) {
            enclose = -((Operation) min).getPriority() < -getPriority();
        }
        this.min = min;
        this.left = left;
    }

    protected abstract int apply(int x);
    protected abstract BigInteger apply(BigInteger x);

    @Override
    public int evaluate(int x, int y, int z) {
        return apply(min.evaluate(x, y, z));
    }

    @Override
    public BigInteger evaluate(BigInteger x) {
        return apply(min.evaluate(x));
    }

    @Override
    public int evaluate(int x) {
        return apply(min.evaluate(x));
    }

    @Override
    public void fastToString(StringBuilder sb) {
        if (left) {
            sb.append(getOperation());
        }
        sb.append('(');
        min.fastToString(sb);
        sb.append(')');
        if (!left) {
            sb.append(getOperation());
        }
    }

    @Override
    public void fastToMiniString(StringBuilder sb) {
        if (left) {
            sb.append(getOperation());
            if (!enclose) {
                sb.append(" ");
            }
        }
        fastBrackets(enclose, sb, min);
        if (!left) {
            if (!enclose) {
                sb.append(" ");
            }
            sb.append(getOperation());
        }
    }

    @Override
    public boolean equals(Object other) {
        if (null == other) {
            return false;
        }
        if (this.getClass() == other.getClass()) {
            return min.equals(((UnaryOperation) other).min);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 17 * min.hashCode() + this.getClass().hashCode();
    }

    public PolyExpression get() {
        return min;
    }
}