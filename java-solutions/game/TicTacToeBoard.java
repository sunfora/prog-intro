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

    public TicTacToeBoard(int n, int m, int k) {
        super(new CellField(n, m));
        this.k = k;
        field = (CellField) super.field;
        pretty = new Prettify(field)
            .grid("ort", " ")
            .displayUniversal(CELL_TO_STRING)
            .showAxis(true);
    }

    @Override
    protected boolean checkWin(Move move) {
        return checkAt(move.getCol(), move.getRow());
    }

    @Override
    protected boolean checkDraw(Move move) {
        return field.isFilled();
    }

    @Override
    public String toString() {
        String result = pretty.toString()
            + System.lineSeparator()
            + String.format("TicTacToeBoard[turn = %s]", CELL_TO_STRING.get(turn));
        return result;
    }

    public boolean checkAt(int i, int j) {
        return (field.get(i, j) != Cell.E
                 && (moveAndCount(i, j, 1,  0)
                 ||  moveAndCount(i, j, 0,  1)
                 ||  moveAndCount(i, j, 1,  1)
                 ||  moveAndCount(i, j, 1, -1)));
    }

    private boolean moveAndCount(int i, int j, int dx, int dy) {
        int cnt = 1;
        for (int t = 0; t < 2; ++t) {
            int m = (t > 0)? -1: 1;
            int p = 1;
            while (field.getRangeY().contains(j + p*m*dy)
                   && field.getRangeX().contains(i + p*m*dx)
                   && field.get(i, j) == field.get(i + p*m*dx, j + p*m*dy)
            ) {
                if (++cnt >= k) {
                    return true;
                }
                ++p;
            }
        }
        return cnt >= k;
    }
}