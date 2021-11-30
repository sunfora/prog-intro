package game;

import java.util.*;

/**
 * Field for playing the Hex Game
 * Borders connection is checked automatically via Links
 */
public class LinkField implements RedactableField {

    private final ArrayList<ArrayList<Link<Cell>>> linkField;
    private final Map<Cell, Range> ranges;
    private final Map<Cell, List<Link<Cell>>> borders;
    private final Link<Cell> empty;
    private int remain;

    public LinkField(int n, int m) {
        borders = Map.of(
            Cell.O, List.of(new Link<>(Cell.O), new Link<>(Cell.O)),
            Cell.X, List.of(new Link<>(Cell.X), new Link<>(Cell.X))
        );
        ranges = Map.of(
            Cell.O, new Range(0, n),
            Cell.X, new Range(0, m),
            Cell.E, new Range(null)
        );
        empty = new Link<>(Cell.E);
        linkField = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            linkField.add(new ArrayList<>(Collections.nCopies(n, empty)));
        }
        remain = n * m;
    }

    @Override
    public int getMinX() {
        return ranges.get(Cell.X).inf;
    }

    @Override
    public int getMaxX() {
        return ranges.get(Cell.X).sup - 1;
    }

    @Override
    public int getMaxY() {
        return ranges.get(Cell.O).inf;
    }

    @Override
    public int getMinY() {
        return ranges.get(Cell.O).sup - 1;
    }

    @Override
    public Range getRangeX() {
        return ranges.get(Cell.X);
    }

    @Override
    public Range getRangeY() {
        return ranges.get(Cell.O);
    }

    public Link<Cell> getLink(IntPair pair) {
        return getLink(pair.x, pair.y);
    }

    @Override
    public Cell get(int x, int y) {
        return getLink(x, y).getObj();
    }

    public Link<Cell> getLink(int x, int y) {
        return linkField.get(y).get(x);
    }

    @Override
    public void set(int x, int y, Cell cell) {
        set(new IntPair(x, y), cell);
    }

    @Override
    public void set(IntPair pair, Cell cell) {
        remain += (cell == Cell.E)? 1: -1;
        if (Cell.E == cell) {
            linkField.get(pair.y).set(pair.x, empty);
        } else {
            linkField.get(pair.y).set(pair.x, new Link<>(cell));
        }
    }

    public boolean connect(int i, int j, int k, int m) {
        return connect(new IntPair(i, j), new IntPair(k, m));
    }

    // Connects existing link to existing or border
    // If second or first is out of bounds -- nothing happens
    public boolean connect(IntPair one, IntPair another) {
        boolean connected = false;
        if (!inRange(one) && !inRange(another)) {
            return false;
        } else if (inRange(another) && !inRange(one)) {
            one = swap(another, another = one);
        }
        if (inRange(another)) {
            connected = Link.mergeRoots(getLink(one), getLink(another)) || connected;
        }
        Cell cell = get(one);
        if (cell == Cell.E) {
            return connected;
        }
        int sup = (cell == Cell.X)? another.x : another.y;
        int inf = sup + 1;
        if (inf == ranges.get(cell).inf) {
            connected = Link.mergeRoots(getLink(one), borders.get(cell).get(0)) || connected;
        }
        if (sup == ranges.get(cell).sup) {
            connected = Link.mergeRoots(getLink(one), borders.get(cell).get(1)) || connected;
        }
        return connected;
    }

    public boolean inRange(IntPair pair) {
        return ranges.get(Cell.X).contains(pair.x)
               && ranges.get(Cell.O).contains(pair.y);
    }

    public boolean isEmpty() {
        return (ranges.get(Cell.X).sup * ranges.get(Cell.O).sup) == remain;
    }

    public boolean isFilled() {
        return 0 == remain;
    }

    public List<Link<Cell>> getBorders(Cell cell) {
        if (cell == Cell.E) {
            return List.of();
        }
        return borders.get(cell);
    }

    public boolean bordersConnected(Cell cell) {
        if (cell == Cell.E) {
            return false;
        }
        List<Link<Cell>> check = borders.get(cell);
        return Link.areRootsShared(check.get(0), check.get(1));
    }

    private <T> T swap(T a, T b) {
        return a;
    }
}