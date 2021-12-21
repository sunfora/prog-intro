package expression.exceptions;

import expression.*;
import java.math.BigInteger;

public class Abs extends LeftUnaryOperation {
    public Abs(final PolyExpression expr) {
        super(expr);
    }

    public int apply(final int x) {
        return CheckedMath.abs(x);
    }

    public BigInteger apply(final BigInteger x) {
        return x.abs();
    }

    public int getPriority() {
        return 0;
    }

    public String getOperation() {
        return "abs";
    }
}