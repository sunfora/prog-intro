package expression;

import java.math.BigInteger;

public class Divide extends BinaryOperation {
    private final String op = "/";

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
        return op;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean leftAssociative() {
        return false;
    }

    @Override
    public boolean rightAssociative() {
        return false;
    }
}