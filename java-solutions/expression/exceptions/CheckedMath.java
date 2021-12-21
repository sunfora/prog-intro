package expression.exceptions;

public class CheckedMath {
    private CheckedMath() {}

    private static final int MIN = Integer.MIN_VALUE;
    private static final String OVERFLOW = "%s int overflow %s";
    private static final String B_RES = "%d %s %d : %d";
    private static final String U_RES = "%s %d : %d";

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

    public static int add(int x, int y) {
        if (signOp(x, y) || signEq(x + y, y)) {
            return x + y;
        }
        throw new IntOverflowException(
            String.format(OVERFLOW, "addition", bRes("+", x, y, x + y)),
            x + y
        );
    }

    public static int sub(int x, int y) {
        if (signEq(x, y) || signEq(x - y, x)) {
            return x - y;
        }
        throw new IntOverflowException(
            String.format(OVERFLOW, "subtraction", bRes("-", x, y, x - y)),
            x - y
        );
    }

    public static int neg(int x) {
        switch (x) {
            case MIN:
                throw new IntOverflowException(
                    String.format(OVERFLOW, "negation", uRes("-", x, -x)),
                    -x
                );
        }
        return -x;
    }

    public static int div(int x, int y) {
        switch (y) {
            case 0:
                throw new UndefinedAtException("Division by zero " + x + " / " + y);
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

    public static int mul(int x, int y) {
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

    public static int pow(int x, int y) throws IntOverflowException {
        if (y < 0) {
            throw new UndefinedAtException(
                String.format("Negative power : %d ** %d", x, y)
            );
        } if (y == 0) {
            if (x == 0) {
                throw new UndefinedAtException("0 ** 0");
            } else {
                return 1;
            }
        }
        if (x == 1 || x == 0) {
            return x;
        }
        if (x == -1) {
            return 1 - 2 * (y&1);
        }
        int result = 1;
        int a = 1;
        try {
            for (int p = y; p > 0; p >>= 1) {
                a = (a == 1)? x : mul(a, a);
                if ((p&1) == 1) {
                    result = mul(result, a);
                }
            }
        } catch (IntOverflowException e) {
            throw new IntOverflowException(
                String.format("Overflow while power %d ** %d", x, y),
                e.getOverflowed()
            );
        }
        return result;
    }

    public static int log(int x, int y) {
        if (x <= 0) {
            throw new UndefinedAtException(
                String.format("Negative log arg : %d // %d", x, y)
            );
        }
        if (y < 2) {
            throw new UndefinedAtException(
                String.format("Log base %d // %d", x, y)
            );
        }
        int res;
        for (res = 0; x >= y; x /= y) {
            res++;
        }
        return res;
    }

    public static int abs(int x) {
        if (x == MIN) {
            throw new IntOverflowException(
                String.format(OVERFLOW, "abs", uRes("abs", x, -x)),
                -x
            );
        }
        return x < 0? -x : x;
    }
}