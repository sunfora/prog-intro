package expression.exceptions;

import expression.Multiply;
import expression.PolyExpression;

public class CheckedMultiply extends Multiply {
    public CheckedMultiply(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    @Override
    protected int apply(int x, int y) {
        return CheckedMath.mul(x, y);
    }
}