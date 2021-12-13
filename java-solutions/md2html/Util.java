package md2html;

final public class Util {
    private Util() {}

    public static int swap(int a, int b) {
        return a;
    }

    public static int fall(int a, Object... b) {
        return a;
    }

    public static Integer[] toIntegerArray(int[] array) {
        Integer[] result = new Integer[array.length];
        int j = 0;
        for (int i : array) {
            result[j++] = i;
        }
        return result;
    }
}