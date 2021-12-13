package expression;

import java.util.Objects;
import java.math.BigInteger;

public abstract class BinaryOperation extends Operation {

    protected ToMiniString min1;
    protected ToMiniString min2;

    protected Expression expr1;
    protected Expression expr2;
    protected boolean expr;

    protected TripleExpression exprT1;
    protected TripleExpression exprT2;
    protected boolean triple;

    protected BigIntegerExpression exprB1;
    protected BigIntegerExpression exprB2;
    protected boolean big;

    protected boolean left;
    protected boolean right;

    protected abstract int apply(int x, int y);
    protected abstract BigInteger apply(BigInteger x, BigInteger y);

    public BinaryOperation(ToMiniString min1, ToMiniString min2) {
        this.min1 = Objects.requireNonNull(min1);
        this.min2 = Objects.requireNonNull(min2);

        if (min1 instanceof Operation) {
            left = -((Operation) min1).getPriority() < -getPriority();
        }
        if (min2 instanceof Operation) {
            right = -((Operation) min2).getPriority() < -getPriority();
        }

        if (min1 instanceof Expression && min2 instanceof Expression) {
            expr1 = (Expression) min1;
            expr2 = (Expression) min2;
            expr = true;
        }

        if (min1 instanceof TripleExpression && min2 instanceof TripleExpression) {
            exprT1 = (TripleExpression) min1;
            exprT2 = (TripleExpression) min2;
            triple = true;
        }

        if (min1 instanceof BigIntegerExpression && min2 instanceof BigIntegerExpression) {
            exprB1 = (BigIntegerExpression) min1;
            exprB2 = (BigIntegerExpression) min2;
            big = true;
        }
    }

    @Override
    public int evaluate(int x) {
        if (expr) {
            return apply(expr1.evaluate(x), expr2.evaluate(x));
        }
        ToMiniString cause = (min1 instanceof Expression)? min2 : min1;
        throw new IllegalStateException("Cannot evaluate expression " + cause + " is not Expression");
    }

    @Override
    public int evaluate(int x, int y, int z) {
        if (triple) {
            return apply(
                exprT1.evaluate(x, y, z),
                exprT2.evaluate(x, y, z)
            );
        }
        ToMiniString cause = (min1 instanceof TripleExpression)? min2 : min1;
        throw new IllegalStateException(
            "Cannot evaluate expression "
            + cause
            + " is not TripleExpression"
        );
    }


    @Override
    public BigInteger evaluate(BigInteger x) {
        if (big) {
            return apply(
                exprB1.evaluate(x),
                exprB2.evaluate(x)
            );
        }
        ToMiniString cause = (min1 instanceof BigIntegerExpression)? min2 : min1;
        throw new IllegalStateException(
             "Cannot evaluate expression "
             + cause
             + " is not BigIntegerExpression"
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

    public ToMiniString getLeft() {
        return min1;
    }

    public ToMiniString getRight() {
        return min2;
    }
}