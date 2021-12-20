package expression.exceptions;

public class UndefinedAtException extends IllegalArgumentException {
    public UndefinedAtException(String message) {
        super(message);
    }
}