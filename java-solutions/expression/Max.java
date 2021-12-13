package expression;

import java.math.BigInteger;

public class Max extends BinaryOperation {
    public Max(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    protected boolean rightAssociativeWith(PolyExpression min) {
        return min instanceof Max
            || min instanceof Min; // wtf? a min b max c != a min (b max c)
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public String getOperation() {
        return "max";
    }

    @Override
    protected int apply(int a, int b) {
        return Math.max(a, b);
    }

    @Override
    protected BigInteger apply(BigInteger a, BigInteger b) {
        return a.max(b);
    }
}