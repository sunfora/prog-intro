package game;

public class SequentialPlayer implements Player {

    private Field last;
    private int x;
    private int y;

    @Override
    public Move makeMove(Position position) {
        if (last != position.getField()) {
            last = position.getField();
            x = last.getMinX();
            y = last.getMinY();
        }
        while (last.getRangeY().contains(y)) {
            while (last.getRangeX().contains(x)) {
                final Move move = new Move(x, y, position.getTurn());
                if (position.isValid(move)) {
                    return move;
                }
                x++;
            }
            x = last.getMinX();
            y++;
        }
        throw new AssertionError("No valid moves");
    }
}
