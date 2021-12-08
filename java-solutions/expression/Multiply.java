package expression;

import java.math.BigInteger;

final public class Multiply extends BinaryOperation {

    public Multiply(ToMiniString min1, ToMiniString min2) {
        super(min1, min2);
        if (min2 instanceof Divide) {
            right = true;
        }
    }

    protected int apply(int x, int y) {
        return x * y;
    }

    @Override
    protected BigInteger apply(BigInteger x, BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public String getOperation() {
        return "*";
    }

    @Override
    public int getPriority() {
        return 1;
    }
}