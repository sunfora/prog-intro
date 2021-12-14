package expression.exceptions;

public class CheckedMath {
    private CheckedMath() {}

    private static final int MIN = Integer.MIN_VALUE;
    private static final String OVERFLOW = "%s int overflow %s";
    private static final String B_RES = "%d %s %d : %d";
    private static final String U_RES = "%s %d";

    private static String bRes(String sign, int x, int y, int r) {
        return String.format(B_RES, x, sign, y, r);
    }

    private static String uRes(String sign, int x, int r) {
        return String.format(U_RES, sign, x, r);
    }

    public static boolean signOp(int x, int y) {
        return (x ^ y) < 0;
    }

    public static boolean signEq(int x, int y) {
        return (x ^ y) >= 0;
    }

    public static int add(int x, int y) throws IntOverflowException {
        if (signOp(x, y) || signEq(x + y, y)) {
            return x + y;
        }
        throw new IntOverflowException(
            String.format(OVERFLOW, "addition", bRes("+", x, y, x + y)),
            x + y
        );
    }

    public static int sub(int x, int y) throws IntOverflowException {
        if (signEq(x, y) || signEq(x - y, x)) {
            return x - y;
        }
        throw new IntOverflowException(
            String.format(OVERFLOW, "subtraction", bRes("-", x, y, x - y)),
            x - y
        );
    }

    public static int neg(int x) throws IntOverflowException {
        switch (x) {
            case MIN:
                throw new IntOverflowException(
                    String.format(OVERFLOW, "negation", uRes("-", x, -x)),
                    -x
                );
        }
        return -x;
    }

    public static int div(int x, int y) throws DivisionByZeroException, IntOverflowException {
        switch (y) {
            case 0:
                throw new DivisionByZeroException(x + " / " + y);
            case -1:
                if (x == MIN) {
                    throw new IntOverflowException(
                        String.format(OVERFLOW, "division", bRes("/", x, y, x/y)),
                        x/y
                    );
                }
        }
        return x/y;
    }

    public static int mul(int x, int y) throws IntOverflowException {
        if (x == 0 || y == 0) {
            return 0;
        }
        if (((x * y) / x) == y && signEq(signOp(x, y)? -1 : 1, x*y)) {
            return x * y;
        }
        throw new IntOverflowException(
            String.format(OVERFLOW, "multiplication", bRes("*", x, y, x*y)),
            x*y
        );
    }
}