package game;

public final class IntPair {

    public final int x;
    public final int y;

    public IntPair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public IntPair add(IntPair other) {
        return new IntPair(other.x + x, other.y + y);
    }

    public IntPair sub(IntPair other) {
        return new IntPair(x - other.x, y - other.y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntPair) {
            return (((IntPair)obj).x == x) && (((IntPair)obj).y == y);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 17*Integer.hashCode(x) + y;
    }
}