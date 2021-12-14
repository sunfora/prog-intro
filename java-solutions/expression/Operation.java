package expression;

public abstract class Operation implements PolyExpression {

    abstract public int getPriority();
    abstract public String getOperation();
    abstract public void fastToString(StringBuilder dest);
    abstract public void fastToMiniString(StringBuilder dest);

    protected boolean leftAssociative() {
        return true;
    }

    protected boolean rightAssociative() {
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        fastToString(sb);
        return sb.toString();
    }

    @Override
    public String toMiniString() {
        StringBuilder sb = new StringBuilder();
        fastToMiniString(sb);
        return sb.toString();
    }

    protected void fastBrackets(boolean add, StringBuilder sb, PolyExpression min) {
        if (add) {
            sb.append('(');
        }
        min.fastToMiniString(sb);
        if (add) {
            sb.append(')');
        }
    }
}