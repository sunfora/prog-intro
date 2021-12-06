package game;

import java.util.*;

public class HexBoard extends FieldBoard implements Board {
// #fields:
    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, " ",
            Cell.W, "",
            Cell.B, ""
    );
    private final BaseField<Link<Cell>> linkField;
    private final Prettify pretty;
    private final Map<Cell, Pair<Link<Cell>, Link<Cell>>> borders = Map.of(
        Cell.B, new Pair<>(new Link<>(Cell.B), new Link<>(Cell.B)),
        Cell.W, new Pair<>(new Link<>(Cell.W), new Link<>(Cell.W))
    );
// fields#

// #constructors:
    private HexBoard(int n, int m, BaseField<Link<Cell>> linkField) {
        super(new ProxyLinkField<>(linkField));
        this.linkField = linkField;
        pretty = new HexPrettify()
            .mapByObject(CELL_TO_STRING)
            .showAxis(true)
            .setWidth(2);
    }

    public HexBoard(int n, int m) {
        this(
             n, m,
             new BaseField<Link<Cell>>(
                  1, n,
                  1, m,
                  new Link<Cell>(Cell.E)
             )
        );
    }
// constructors#

// #implemented:
    @Override
    protected boolean checkDraw(Move move) {
        return false;
    }

    @Override
    protected boolean checkWin(Move move) {
        // set connections before
        IntPair point = move.getPos();
        for (int[] ints : new int[][] {{0, 1}, {1, 0}, {1, 1}}) {
            IntPair shift = new IntPair(ints[0], ints[1]);
            for (IntPair neighbor : new IntPair[]{point.add(shift), point.sub(shift)}) {
                connect(point, neighbor);
            }
        }
        return bordersConnected(move.getValue());
    }

    @Override
    public String toString() {
        return pretty.display(field)
               + System.lineSeparator()
               + String.format("HexBoard[turn = %s]", CELL_TO_STRING.get(turn));
    }
// implemented#

// #private:
    private boolean connect(IntPair one, IntPair another) {
        boolean connected = false;
        if (!linkField.inRange(one) && !linkField.inRange(another)) {
            return false;
        } else if (linkField.inRange(another) && !linkField.inRange(one)) {
            one = Util.swap(another, another = one);
        }
        if (linkField.inRange(another)) {
            connected = Link.mergeRoots(linkField.get(one), linkField.get(another))
                        || connected;
        }
        Cell cell = field.get(one);
        if (cell == Cell.E) {
            return connected;
        }
        int sup = (cell == Cell.W)? another.x : another.y;
        Range range = (cell == Cell.W)? field.getRangeX() : field.getRangeY();
        int inf = sup + 1;
        if (inf == range.inf) {
            connected = Link.mergeRoots(linkField.get(one), borders.get(cell).first)
                        || connected;
        }
        if (sup == range.sup) {
            connected = Link.mergeRoots(linkField.get(one), borders.get(cell).second)
                        || connected;
        }
        return connected;
    }

    private boolean bordersConnected(Cell cell) {
        if (cell == Cell.E) {
            return false;
        }
        Pair<Link<Cell>, Link<Cell>> check = borders.get(cell);
        return Link.areRootsShared(check.first, check.second);
    }
// private#
}