package expression;

import java.math.BigInteger;

public class Add extends BinaryOperation {
    private static final String op = "+";

    public Add(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    @Override
    protected int apply(int x, int y) {
        return x + y;
    }

    @Override
    protected BigInteger apply(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    public String getOperation() {
        return op;
    }

    @Override
    public int getPriority() {
        return 2;
    }

}