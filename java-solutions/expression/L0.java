package expression;

import java.math.BigInteger;

public class L0 extends LeftUnaryOperation {
    public L0(PolyExpression min) {
        super(min);
    }

    @Override
    public String getOperation() {
        return "l0";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int apply(int x) {
        return Integer.numberOfLeadingZeros(x);
    }

    @Override
    public BigInteger apply(BigInteger x) {
        return BigInteger.valueOf(apply(x.intValue()));
    }
}