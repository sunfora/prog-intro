package game;

import java.util.*;

public class HexBoard extends FieldBoard implements Board {

    private static final Map<Cell, String> CELL_TO_STRING = Map.of(
            Cell.E, " ",
            Cell.X, "",
            Cell.O, ""
    );

    private final LinkField field;
    private final Prettify pretty;

    public HexBoard(int n, int m) {
        super(new LinkField(n, m));
        field = (LinkField) super.field;
        pretty = new Prettify(field)
            .grid("hex")
            .displayUniversal(CELL_TO_STRING)
            .showAxis(true)
            .setWidth(2);
    }

    @Override
    protected boolean checkDraw(Move move) {
        return false;
    }

    @Override
    protected boolean checkWin(Move move) {
        // set connections before
        IntPair pair = new IntPair(move.getCol(), move.getRow());
        for (int[] ints : new int[][] {{0, 1}, {1, 0}, {1, 1}}) {
            IntPair shift = new IntPair(ints[0], ints[1]);
            for (IntPair neighbor : new IntPair[]{pair.add(shift), pair.sub(shift)}) {
                field.connect(pair, neighbor);
            }
        }
        return field.bordersConnected(move.getValue());
    }

    @Override
    public String toString() {
        String result = pretty.toString()
                        + System.lineSeparator()
                        + String.format("HexBoard[turn = %s]", CELL_TO_STRING.get(turn));
        return result;
    }
}