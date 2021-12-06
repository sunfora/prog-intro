package game;

public class Util {
    private Util() {}

    public static int max() {
        throw new IllegalArgumentException("max 0 arguments");
    }

    public static int max(int... nums) {
        int acum = Integer.MIN_VALUE;
        for (int i = 0; i < nums.length; ++i) {
            acum = Math.max(acum, nums[i]);
        }
        return acum;
    }

    public static <T> T swap(T a, T b) {
        return a;
    }

    public static <T> T fall(T a, Object... b) {
        return a;
    }

    public static int cdiv(int a, int b) {
        return (a + b - 1)/b;
    }
}