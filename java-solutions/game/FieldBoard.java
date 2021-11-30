package game;

public abstract class FieldBoard implements Board {

    protected final RedactableField field;
    protected final Position pos;
    protected Cell turn;


    public FieldBoard(RedactableField field) {
        this.field = field;
        this.pos = new BoardViewer();
        turn = Cell.X;
    }

    @Override
    public Position getPosition() {
        return pos;
    }

    @Override
    public GameResult makeMove(Move move) {
        if (!pos.isValid(move)) {
            return GameResult.LOOSE;
        }

        field.set(move.getCol(), move.getRow(), move.getValue());

        if (checkWin(move)) {
            return GameResult.WIN;
        }

        if (checkDraw(move)) {
            return GameResult.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return GameResult.UNKNOWN;
    }

    protected abstract boolean checkDraw(Move move);

    protected abstract boolean checkWin(Move move);

    protected class BoardViewer implements Position {

        @Override
        public Cell getCell(int column, int row) {
            return field.get(column, row);
        }

        public boolean isValid(final Move move) {
            return field.getRangeX().contains(move.getCol())
                && field.getRangeY().contains(move.getRow())
                && field.get(move.getCol(), move.getRow()) == Cell.E
                && turn == move.getValue();
        }

        @Override
        public Cell getTurn() {
            return turn;
        }

        @Override
        public String toString() {
            return FieldBoard.this.toString();
        }
    }
}