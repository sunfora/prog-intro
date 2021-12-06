package game;

import java.util.*;

/**
* Rectangular field to store values
*/
public interface Field<C> extends Iterable<C> {
   /**
   * Returns value or null for every cell in rangeX, rangeY
   * throws IndexOutOfBoundsException if (x, y) not lies in field
   */
    C get(int x, int y) throws IndexOutOfBoundsException;

    // Bounds getters
    int getMinX();
    int getMaxX();
    int getMinY();
    int getMaxY();

    // Bounds as ranges
    default Range getRangeY() {
        return new Range(getMinY(), getMaxY() + 1);
    }
    default Range getRangeX() {
        return new Range(getMinX(), getMaxX() + 1);
    }

    // Getter for point
    default C get(IntPair pos) {
        return get(pos.x, pos.y);
    }

    // Default iterator over field
    default Iterator<C> iterator() {
        return new FieldIterator<>(this);
    }

    static <T> Field<T> unmodifiable(Field<T> field) {
        return new ProxyField<>(field);
    }
}

/**
* Field iterator by default, iterates from left-lower to top-right corner
*/
class FieldIterator<C> implements Iterator<C> {
// #fields:
    private final Field<C> field;
    private IntPair current;
    private boolean end;
    private final IntPair END;
// fields#

// #public:
    public FieldIterator(Field<C> field) {
        this.field = field;
        current = new IntPair(field.getMinX(), field.getMinY());
        END = new IntPair(field.getMaxX(), field.getMaxY());
    }

    public boolean hasNext() {
        return end;
    }

    public C next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Field exceeded");
        }
        return upd(field.get(current));
    }
// public#

// #private:
    private C upd(final C fall) {
        if (END.equals(current)) {
            end = true;
        }
        current = current.x < field.getMaxX()
                  ? new IntPair(current.x + 1, current.y)
                  : new IntPair(field.getMinX(), current.y + 1);
        return fall;
    }
// private#
}