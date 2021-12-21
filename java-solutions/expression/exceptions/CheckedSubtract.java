package expression.exceptions;

import expression.Subtract;
import expression.PolyExpression;

public class CheckedSubtract extends Subtract {
    public CheckedSubtract(PolyExpression min1, PolyExpression min2) {
        super(min1, min2);
    }

    @Override
    protected int apply(int x, int y) {
        return CheckedMath.sub(x, y);
    }
}