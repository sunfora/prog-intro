package game;

public class Move {

    private final int y;
    private final int x;
    private final Cell value;

    public Move(int x, int y, Cell value) {
        this.y = y;
        this.x = x;
        this.value = value;
    }

    public Move(IntPair point, Cell value) {
        this(point.x, point.y, value);
    }

    public int getX() {
        return y;
    }

    public int getY() {
        return x;
    }

    public IntPair getPos() {
        return new IntPair(x, y);
    }

    public Cell getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("Move(%s, %d, %d)", value, x, y);
    }
}
