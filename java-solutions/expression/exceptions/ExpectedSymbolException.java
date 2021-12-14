package expression.exceptions;

public class ExpectedSymbolException extends OffsetParseException {
    private final char expected;
    private final char found;

    public ExpectedSymbolException(int pos, char expected, char found) {
        super(pos, "expected : '" + expected + "' found : '" + found + "'");
        this.expected = expected;
        this.found = found;
    }

    public char getExpected() {
        return expected;
    }

    public char getFound() {
        return found;
    }
}