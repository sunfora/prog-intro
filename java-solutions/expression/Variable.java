package expression;

import java.math.BigInteger;

final public class Variable implements PolyExpression {
    private final String variable;

    public Variable(char variable) {
        this(Character.toString(variable));
    }

    public Variable(String variable) {
        this.variable = variable;
    }

    @Override
    public BigInteger evaluate(BigInteger x) {
        if (variable == "x") {
            return x;
        }
        throw notEvaluated();
    }

    @Override
    public int evaluate(int x) {
        if (variable == "x") {
            return x;
        }
        throw notEvaluated();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (variable) {
            case "x":
                return x;
            case "y":
                return y;
            case "z":
                return z;
            default:
                throw notEvaluated();
        }
    }

    private IllegalStateException notEvaluated() {
        return new IllegalStateException("Variable" + variable + " is not evaluated");
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String toString() {
        return variable;
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public void fastToMiniString(StringBuilder sb) {
        sb.append(variable);
    }

    @Override
    public void fastToString(StringBuilder sb) {
        sb.append(variable);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Variable) {
            return variable.equals(((Variable) other).variable);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return variable.hashCode();
    }
}