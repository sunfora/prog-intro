package expression;

import java.math.BigInteger;

final public class Variable implements Expression, TripleExpression, BigIntegerExpression {
    private final String var;

    public Variable(char var) {
        this(Character.toString(var));
    }

    public Variable(String var) {
        switch (var.toLowerCase()) {
            case "x":
            case "y":
            case "z":
                this.var = var;
                break;
            default:
                throw new IllegalArgumentException("Not supported Variable");
        }

    }

    @Override
    public BigInteger evaluate(BigInteger x) {
        if (var == "x") {
            return x;
        }
        throw notEvaluated();
    }

    @Override
    public int evaluate(int x) {
        if (var == "x") {
            return x;
        }
        throw notEvaluated();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        switch (var) {
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
        return new IllegalStateException("Variable" + var + " is not evaluated");
    }

    @Override
    public String toString() {
        return var;
    }

    @Override
    public String toMiniString() {
        return toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Variable) {
            return var.equals(((Variable) other).var);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return var.hashCode();
    }
}