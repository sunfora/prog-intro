package expression;

import java.math.BigInteger;

public class Max extends BinaryOperation {
    public Max(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    @Override
    public int getPriority() {
        return 5;
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