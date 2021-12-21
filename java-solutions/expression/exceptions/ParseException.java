package expression.exceptions;

public class ParseException extends Exception {
    private final int offset;
    private final String raw;

    public ParseException(int offset, String message, Throwable cause) {
        super(offset + " : " + message, cause);
        this.offset = offset;
        this.raw = message;
    }

    public ParseException(int offset, String message) {
        super(offset + " : " + message);
        this.offset = offset;
        this.raw = message;
    }

    public int getOffset() {
        return offset;
    }

    public String getRawMessage() {
        return raw;
    }
}