package expression.exceptions;

public class DivisionByZeroException extends ArithmeticException {
    public DivisionByZeroException(String message) {
        super(message);
    }

    public DivisionByZeroException() {
        super();
    }
}