package expression.exceptions;

import expression.*;
import java.math.BigInteger;

public class Pow extends BinaryOperation {
    public Pow(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    public String getOperation() {
        return "**";
    }

    public int apply(int x, int y) {
        return CheckedMath.pow(x, y);
    }

    public BigInteger apply(BigInteger x, BigInteger y) {
        throw new UnsupportedOperationException("BigInteger ** BigInteger");
    }

    public int getPriority() {
        return 1;
    }

    @Override
    public boolean isAssociative() {
        return false;
    }

    @Override
    public boolean rightAssociative() {
        return false;
    }

    @Override
    public boolean leftAssociative() {
        return false;
    }
}