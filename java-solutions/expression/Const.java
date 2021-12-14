package expression;

import java.math.BigInteger;

final public class Const implements PolyExpression {
    private final BigInteger value;

    public Const(int value) {
        this.value = BigInteger.valueOf(value);
    }

    public Const(BigInteger value) {
        this.value = value;
    }

    @Override
    public BigInteger evaluate(BigInteger x) {
        return value;
    }

    @Override
    public int evaluate(int x) {
        return value.intValue();
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return value.intValue();
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public String toMiniString() {
        return value.toString();
    }

    @Override
    public void fastToMiniString(StringBuilder sb) {
        sb.append(value);
    }

    @Override
    public void fastToString(StringBuilder sb) {
        sb.append(value);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Const) {
            return value.equals(((Const) other).value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}