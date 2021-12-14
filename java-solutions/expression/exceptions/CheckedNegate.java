package expression.exceptions;

import expression.Neg;
import expression.PolyExpression;

public class CheckedNegate extends Neg {
    public CheckedNegate(PolyExpression min1) {
        super(min1);
    }

    @Override
    protected int apply(int x) {
        return CheckedMath.neg(x);
    }
}