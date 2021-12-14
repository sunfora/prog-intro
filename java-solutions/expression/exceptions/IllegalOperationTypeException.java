package expression.exceptions;

public class IllegalOperationTypeException extends OffsetParseException {
    public IllegalOperationTypeException(int pos, String message) {
        super(pos, message);
    }
}