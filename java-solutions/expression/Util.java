package expression;

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

    public static int swap(int a, int b) {
        return a;
    }

    public static char swap(char a, char b) {
        return a;
    }

    public static int fall(int a, Object b) {
        return a;
    }

    public static char fall(char a, Object b) {
        return a;
    }

    public static boolean isWord(String word) {
        if (word == null) {
            return false;
        }
        for (char c : word.toCharArray()) {
            if (!isWord(c)) {
                return false;
            }
        }
        return word.length() > 0;
    }

    public static boolean isWord(char c) {
        c = Character.toLowerCase(c);
        return between(c, '0', '9') || between(c, 'a', 'z');
    }

    public static boolean between(final char c, final char from, final char to) {
        return from <= c && c <= to;
    }
}