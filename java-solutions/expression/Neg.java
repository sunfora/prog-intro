package expression;

import java.math.BigInteger;

public class Neg extends LeftUnaryOperation {
    public Neg(PolyExpression min) {
        super(min);
    }

    @Override
    public String getOperation() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    protected int apply(int x) {
        return -x;
    }

    @Override
    protected BigInteger apply(BigInteger x) {
        return x.negate();
    }
}