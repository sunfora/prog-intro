package game;

import java.util.*;

public class CellField implements RedactableField {

    private final ArrayList<ArrayList<Cell>> field;
    public final Range xrange;
    public final Range yrange;
    private int remain;

    public CellField(int n, int m) {
        field = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            field.add(new ArrayList<>(Collections.nCopies(m, Cell.E)));
        }
        this.xrange = new Range(0, m);
        this.yrange = new Range(0, n);
        remain = n*m;
    }

    public Cell get(int i, int j) {
        return field.get(j).get(i);
    }

    public void set(int i, int j, Cell cell) {
        remain += cell == Cell.E? 1: -1;
        field.get(j).set(i, cell);
    }

    public boolean isEmpty() {
        return xrange.sup*yrange.sup == remain;
    }

    public boolean isFilled() {
        return 0 == remain;
    }

    @Override
    public int getMinX() {
        return xrange.inf;
    }

    @Override
    public int getMaxX() {
        return xrange.sup - 1;
    }

    @Override
    public int getMinY() {
        return yrange.inf;
    }

    @Override
    public int getMaxY() {
        return yrange.sup - 1;
    }

    @Override
    public Range getRangeX() {
        return xrange;
    }

    @Override
    public Range getRangeY() {
        return yrange;
    }
}