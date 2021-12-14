package expression.exceptions;

public class OverflowOperationException extends ResultOperationException {
    public OverflowOperationException(String message, Throwable cause, int result) {
        super(message, cause, result);
    }
}