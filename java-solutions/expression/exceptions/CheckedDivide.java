package expression.exceptions;

import expression.Divide;
import expression.PolyExpression;

public class CheckedDivide extends Divide {
    public CheckedDivide(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    @Override
    protected int apply(int x, int y) {
        return CheckedMath.div(x, y);
    }
}