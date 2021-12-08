package expression;

import java.math.BigInteger;

public class Min extends BinaryOperation {
    public Min(ToMiniString min1, ToMiniString min2) {
        super(min1, min2);
    }

    @Override
    public int getPriority() {
        return 3;
    }

    @Override
    public String getOperation() {
        return "min";
    }

    @Override
    protected int apply(int a, int b) {
        return Math.min(a, b);
    }

    @Override
    protected BigInteger apply(BigInteger a, BigInteger b) {
        return a.min(b);
    }
}