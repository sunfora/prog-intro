package expression.exceptions;

import expression.*;
import java.math.BigInteger;

public class LShift extends BinaryOperation {
    public LShift(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    public String getOperation() {
        return "<<";
    }

    public int apply(int x, int y) {
        return x << y;
    }

    public BigInteger apply(BigInteger x, BigInteger y) {
        throw new UnsupportedOperationException("BigInteger << BigInteger");
    }

    public int getPriority() {
        return 4;
    }

    public boolean leftAssociative() {
        return false;
    }

    public boolean isAssociative() {
        return false;
    }

    public boolean rightAssociative() {
        return false;
    }
}