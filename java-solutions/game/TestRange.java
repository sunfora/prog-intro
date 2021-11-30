package game;

public class TestRange {
    public static void main(String args[]) {
        Range range = new Range(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        for (int i : range) {
            System.out.println(i);
        }
    }
}