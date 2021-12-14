package expression.exceptions;

public class ResultOperationException extends OperationException {
    private final int result;

    public ResultOperationException(String message, Throwable cause, int result) {
        super(message, cause);
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}