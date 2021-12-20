package expression.exceptions;

import expression.*;
import java.math.BigInteger;

public class Log extends BinaryOperation {
    public Log(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    public int apply(int x, int y) {
        return CheckedMath.log(x, y);
    }

    public BigInteger apply(BigInteger x, BigInteger y) {
        throw new UnsupportedOperationException("BigInteger // BigInteger");
    }

    public String getOperation() {
        return "//";
    }

    public int getPriority() {
        return 1;
    }

    public boolean isAssociative() {
        return false;
    }

    public boolean rightAssociative() {
        return false;
    }

    public boolean leftAssociative() {
        return false;
    }
}