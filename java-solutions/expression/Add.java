package expression;

import java.math.BigInteger;

final public class Add extends BinaryOperation {
    public Add(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    protected boolean rightAssociativeWith(PolyExpression min) {
        return (min instanceof Add || min instanceof Subtract);
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
    public String getOperation() {
        return "+";
    }

    @Override
    public int getPriority() {
        return 2;
    }

}