package expression.exceptions;

public class EndExpectedException extends OffsetParseException {
    public EndExpectedException(int pos, String message) {
        super(pos, message);
    }
}