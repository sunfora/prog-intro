package expression;

import java.math.BigInteger;

final public class Add extends BinaryOperation {
    public Add(ToMiniString min1, ToMiniString min2) {
        super(min1, min2);
    }

    @Override
    protected int apply(int x, int y) {
        return x + y;
    }

    @Override
    protected BigInteger apply(BigInteger x, BigInteger y) {
        return x.add(y);
    }

    @Override
    protected String getOperation() {
        return "+";
    }

    @Override
    public int getPriority() {
        return 2;
    }

}