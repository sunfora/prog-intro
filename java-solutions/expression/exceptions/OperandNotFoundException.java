package expression.exceptions;

public class OperandNotFoundException extends ParseException {
    public OperandNotFoundException(int pos, String message) {
        super(pos, message);
    }

    public OperandNotFoundException(int pos, String message, Throwable cause) {
        super(pos, message, cause);
    }
}