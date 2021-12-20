package expression.exceptions;

public class ExpectedSymbolException extends OffsetParseException {
    private final char expected;
    private final char found;

    public ExpectedSymbolException(int pos, char expected, char found, String message) {
        super(pos, "expected : '" + expected + "' found : '" + found + "' " + message);
        this.expected = expected;
        this.found = found;
    }

    public ExpectedSymbolException(int pos, char expected, char found) {
        this(pos, expected, found, "");
    }

    public char getExpected() {
        return expected;
    }

    public char getFound() {
        return found;
    }
}