package expression;

import java.math.BigInteger;

final public class Subtract extends BinaryOperation {
    public Subtract(ToMiniString min1, ToMiniString min2) {
        super(min1, min2);
        if (min2 instanceof Operation) {
            if (getPriority() == ((Operation) min2).getPriority()) {
                right = true;
            }
        }
    }

    @Override
    protected int apply(int x, int y) {
        return x - y;
    }

    @Override
    protected BigInteger apply(BigInteger x, BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public String getOperation() {
        return "-";
    }

    @Override
    public int getPriority() {
        return 2;
    }

}