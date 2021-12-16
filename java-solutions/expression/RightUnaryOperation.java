package expression;

abstract public class RightUnaryOperation extends UnaryOperation {
    public RightUnaryOperation(PolyExpression min) {
        super(min, false);
    }

    @Override
    protected boolean leftAssociative() {
        return false;
    }
}