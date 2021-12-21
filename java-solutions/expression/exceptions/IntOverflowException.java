package expression.exceptions;

public class IntOverflowException extends ArithmeticException {
    private final int overflowed;

    public IntOverflowException(String message, int overflowed) {
        super(message);
        this.overflowed = overflowed;
    }

    public IntOverflowException(int overflowed) {
        super();
        this.overflowed = overflowed;
    }

    public int getOverflowed() {
        return overflowed;
    }
}