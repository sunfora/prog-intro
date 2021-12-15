package expression;

import java.math.BigInteger;

public class Multiply extends BinaryOperation {
    private final static String op = "*";

    public Multiply(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    protected int apply(int x, int y) {
        return x * y;
    }

    @Override
    protected BigInteger apply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public String getOperation() {
        return op;
    }

    @Override
    public int getPriority() {
        return 1;
    }
}