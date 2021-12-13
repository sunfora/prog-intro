package expression;

abstract public class LeftUnaryOperation extends UnaryOperation {
    public LeftUnaryOperation(PolyExpression min) {
        super(min, true);
    }
}