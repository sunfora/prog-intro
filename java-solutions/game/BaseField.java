package game;

import java.util.*;

/**
* Basic implementation of RedactableField
*/
public class BaseField<C> implements RedactableField<C> {
// #fields:
    protected final ArrayList<ArrayList<C>> field;

    public final Range xrange;
    public final Range yrange;
    protected final C defaultValue;
    protected long remain;

    protected final IntPair corner;
// fields#

// #constructors:
    public BaseField(Range xrange, Range yrange, C defaultValue) {
        this.xrange = xrange;
        this.yrange = yrange;
        this.defaultValue = defaultValue;
        field = new ArrayList<>();
        for (int i = 0; i < yrange.length(); ++i) {
            field.add(new ArrayList<>(Collections.nCopies(xrange.length(), defaultValue)));
        }
        remain = 1L * xrange.length() * yrange.length();
        corner = new IntPair(getMinX(), getMinY());
    }

    public BaseField(Range xrange, Range yrange) {
        this(xrange, yrange, null);
    }

    public BaseField(int xMin, int xMax, int yMin, int yMax, C defaultValue) {
        this(new Range(xMin, xMax + 1), new Range(yMin, yMax + 1), defaultValue);
    }

    public BaseField(int xMin, int xMax, int yMin, int yMax) {
        this(xMin, xMax, yMin, yMax, null);
    }

    public BaseField(int xMax, int yMax, C defaultValue) {
        this(0, xMax, 0, yMax, defaultValue);
    }

    public BaseField(int xMax, int yMax) {
        this(xMax, yMax, null);
    }
// constructors#

// #public:
    public boolean isEmpty() {
        return xrange.sup*yrange.sup == remain;
    }

    public boolean isFilled() {
        return 0L == remain;
    }

    public boolean inRange(IntPair point) {
        return xrange.contains(point.x) && yrange.contains(point.y);
    }
// public#

// #implemented:
    @Override
    public C get(int x, int y) {
        IntPair p = handle(x, y);
        return field.get(p.y).get(p.x);
    }

    @Override
    public void set(int x, int y, C value) {
        IntPair p = handle(x, y);
        remain += (defaultValue == value)? 1: -1;
        field.get(p.y).set(p.x, value);
    }

    @Override public int getMinX() {return xrange.inf;}
    @Override public int getMinY() {return yrange.inf;}
    @Override public int getMaxY() {return yrange.sup - 1;}
    @Override public int getMaxX() {return xrange.sup - 1;}
// implemented#

// #protected:
    protected IntPair handle(int x, int y) {
        return handle(new IntPair(x, y));
    }

    protected IntPair handle(IntPair point) {
        return ensure(point).sub(corner);
    }

    protected IntPair ensure(IntPair point) {
        if (!inRange(point)) {
            throw new IndexOutOfBoundsException(
                "Expected values of x : " + xrange + ", "
                + "expected values of y : " + yrange
                + " got : " + point
            );
        }
        return point;
    }
// protected#
}