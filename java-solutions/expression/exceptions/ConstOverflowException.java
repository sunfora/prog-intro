package expression.exceptions;

public class ConstOverflowException extends ParseException {
    public ConstOverflowException(int pos, String message, Throwable cause) {
        super(pos, message, cause);
    }

    public ConstOverflowException(int pos, String message) {
        super(pos, message);
    }
}