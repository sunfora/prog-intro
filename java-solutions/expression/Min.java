package expression;

import java.math.BigInteger;

public class Min extends BinaryOperation {
    public Min(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    protected boolean rightAssociativeWith(PolyExpression min) {
        return min instanceof Min
            || min instanceof Max; // wtf?
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