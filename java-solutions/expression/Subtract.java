package expression;

import java.math.BigInteger;

final public class Subtract extends BinaryOperation {
    public Subtract(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    @Override
    protected int apply(int x, int y) {
        return x - y;
    }

    @Override
    protected BigInteger apply(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public String getOperation() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 2;
    }

}