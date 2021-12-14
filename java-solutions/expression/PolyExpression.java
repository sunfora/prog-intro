package expression;

public interface PolyExpression extends TripleExpression, Expression, BigIntegerExpression {
    public void fastToMiniString(StringBuilder dest);
    public void fastToString(StringBuilder dest);
    public int getPriority();
}