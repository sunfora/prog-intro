package expression.exceptions;

public class NotAnOperationException extends ParseException {
    private final String sign;

    public NotAnOperationException(int pos, String sign) {
        super(pos, sign + " is not an operation");
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }
}