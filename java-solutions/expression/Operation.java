package expression;

public abstract class Operation implements PolyExpression {

    abstract public int getPriority();
    abstract public String getOperation();
    abstract protected void fastToString(StringBuilder dest);
    abstract protected void fastToMiniString(StringBuilder dest);

    protected boolean leftAssociative() {
        return true;
    }

    protected boolean rightAssociative() {
        return true;
    }

    protected boolean selfAssociative() {
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

    protected void fastMini(ToMiniString min, StringBuilder sb) {
        if (min instanceof Operation) {
            ((Operation) min).fastToMiniString(sb);
        } else {
            sb.append(min.toMiniString());
        }
    }

    protected void fast(boolean mini, ToMiniString min, StringBuilder sb) {
        if (min instanceof Operation) {
            if (mini) {
                ((Operation) min).fastToMiniString(sb);
            } else {
                ((Operation) min).fastToString(sb);
            }
        } else {
            sb.append(mini? min.toMiniString() : min.toString());
        }
    }

    protected void fastBrackets(boolean add, boolean mini, StringBuilder sb, ToMiniString min) {
        if (add) {
            sb.append('(');
        }
        fast(mini, min, sb);
        if (add) {
            sb.append(')');
        }
    }
}