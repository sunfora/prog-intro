package expression;

import java.math.BigInteger;

public class T0 extends LeftUnaryOperation {
    public T0(PolyExpression min) {
        super(min);
    }

    @Override
    public String getOperation() {
        return "t0";
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public int apply(int x) {
        return Integer.numberOfTrailingZeros(x);
    }

    @Override
    public BigInteger apply(BigInteger x) {
        return BigInteger.valueOf(x.getLowestSetBit());
    }
}