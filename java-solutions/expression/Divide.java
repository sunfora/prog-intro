package expression;

import java.math.BigInteger;

final public class Divide extends BinaryOperation {
    public Divide(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    @Override
    protected int apply(int x, int y) {
        return x/y;
    }

    @Override
    protected BigInteger apply(BigInteger x, BigInteger y) {
        return x.divide(y);
    }

    @Override
    public String getOperation() {
        return "/";
    }

    @Override
    public int getPriority() {
        return 1;
    }

}