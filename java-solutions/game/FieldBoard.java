package game;

/**
* Abstract board on every Field<Cell> field for Tic-Tac-Toe and such
*/
public abstract class FieldBoard implements Board {
// #fields:
    protected final RedactableField<Cell> field;
    protected final Position pos;
    protected Cell turn;
// fields#

// #constructors:
    public FieldBoard(RedactableField<Cell> field) {
        this.field = field;
        this.pos = new BoardViewer();
        turn = Cell.W;
    }
// constructors#

// #implemented:
    @Override
    public Position getPosition() {
        return pos;
    }

    @Override
    public GameResult makeMove(Move move) {
        if (!pos.isValid(move)) {
            return GameResult.LOOSE;
        }

        field.set(move.getPos(), move.getValue());

        if (checkWin(move)) {
            return GameResult.WIN;
        }
        if (checkDraw(move)) {
            return GameResult.DRAW;
        }

        turn = (turn == Cell.W)? Cell.B : Cell.W;
        return GameResult.UNKNOWN;
    }
// implemented#

// #abstract:
    protected abstract boolean checkDraw(Move move);
    protected abstract boolean checkWin(Move move);
// abstract#

    /**
    * BoardViewer implements Position using board's underlying field
    */
    protected class BoardViewer implements Position {
        private final Field<Cell> field;

        public BoardViewer() {
            this.field = Field.unmodifiable(FieldBoard.this.field);
        }

        @Override
        public Field<Cell> getField() {
            return field;
        }

        @Override
        public boolean isValid(final Move move) {
            return field.getRangeX().contains(move.getX())
                && field.getRangeY().contains(move.getY())
                && field.get(move.getPos()) == Cell.E
                && turn == move.getValue();
        }

        @Override
        public Cell getTurn() {
            return turn;
        }
    }
}