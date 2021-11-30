package game;

/**
 * Field is a rectangular object in integer coordinate system
 */
public interface Field {

    public Cell get(int x, int y);

    public int getMinX();

    public int getMaxX();

    public int getMinY();

    public int getMaxY();

    default Range getRangeY() {
        return new Range(getMinY(), getMaxY() + 1);
    }

    default Range getRangeX() {
        return new Range(getMinX(), getMaxX() + 1);
    }

    default Cell get(IntPair pos) {
        return get(pos.x, pos.y);
    }

}