package game;

import java.util.*;

public class TicTacToeBoard extends FieldBoard {
    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, "-",
            Cell.X, "x",
            Cell.O, "o"
    );
    private CellField field;
    private final int k;
    private final Prettify pretty;

    public TicTacToeBoard(final int n, final int m, final int k) {
        super(new CellField(n, m));
        this.k = k;
        field = (CellField) super.field;
        pretty = new Prettify(field)
            .grid("ort", " ")
            .displayUniversal(CELL_TO_STRING)
            .showAxis(true);
    }

    @Override
    protected boolean checkWin(final Move move) {
        return checkAt(move.getCol(), move.getRow());
    }

    @Override
    protected boolean checkDraw(final Move move) {
        return field.isFilled();
    }

    @Override
    public String toString() {
        final String result = pretty.toString()
            + System.lineSeparator()
            + String.format("TicTacToeBoard[turn = %s]", CELL_TO_STRING.get(turn));
        return result;
    }

    public boolean checkAt(final int i, final int j) {
        return (field.get(i, j) != Cell.E
                 && (moveAndCount(i, j, 1,  0)
                 ||  moveAndCount(i, j, 0,  1)
                 ||  moveAndCount(i, j, 1,  1)
                 ||  moveAndCount(i, j, 1, -1)));
    }

    // :NOTE: Упростить
    private boolean moveAndCount(final int x, final int y, final int dx, final int dy) {
        int cnt = 1;
        for (int t = 0; t < 2; ++t) {
            final int m = (t > 0)? -1: 1;
            int p = 1;
            while (field.getRangeY().contains(y + p*m*dy)
                   && field.getRangeX().contains(x + p*m*dx)
                   && field.get(x, y) == field.get(x + p*m*dx, y + p*m*dy)
            ) {
                if (++cnt >= k) {
                    return true;
                }
                p++;
            }
        }
        return cnt >= k;
    }
}