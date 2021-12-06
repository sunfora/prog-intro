package game;

import java.util.*;

public class TicTacToeBoard extends FieldBoard {
    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, "-",
            Cell.W, "x",
            Cell.B, "o"
    );
    private BaseField<Cell> field;
    private final int k;
    private final Prettify pretty;

    public TicTacToeBoard(final int n, final int m, final int k) {
        super(new BaseField<>(1, n, 1, m, Cell.E));
        this.k = k;
        field = (BaseField<Cell>) super.field;
        pretty = new OrtPrettify()
            .mapByObject(CELL_TO_STRING)
            .showAxis(true)
            .setSep(" ");
    }

    @Override
    protected boolean checkWin(final Move move) {
        return checkAt(move.getPos());
    }

    @Override
    protected boolean checkDraw(final Move move) {
        return field.isFilled();
    }

    @Override
    public String toString() {
        return pretty.display(field)
               + System.lineSeparator()
               + String.format("TicTacToeBoard[turn = %s]", CELL_TO_STRING.get(turn));
    }

    public boolean checkAt(IntPair point) {
        return checkAt(point.x, point.y);
    }

    public boolean checkAt(int x, int y) {
        return (field.get(x, y) != Cell.E
                 && (moveAndCount(x, y, 1,  0)
                 ||  moveAndCount(x, y, 0,  1)
                 ||  moveAndCount(x, y, 1,  1)
                 ||  moveAndCount(x, y, 1, -1)));
    }

    // :NOTE: Упростить
    // done
    private boolean moveAndCount(int x, int y, int dx, int dy) {
        IntPair point = new IntPair(x, y);
        IntPair sh = new IntPair(dx, dy);
        Cell value = field.get(point);
        int cnt = 1;
        for (int direction : new int[] {1, -1}) {
            IntPair next = point;
            IntPair shift = sh.times(direction);
            boolean same;
            do {
                next = next.add(shift);
                same = field.inRange(next) && (value == field.get(next));
            } while (same && (++cnt < k));
        }
        return cnt >= k;
    }
}