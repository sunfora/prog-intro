package game;

import java.util.List;
import java.io.PrintStream;

public class HexTournament extends Circle31Tournament {
    private final int boardSize;
    public HexTournament(int boardSize, List<Player> players, PrintStream out) {
        super(players, out);
        this.boardSize = boardSize;
    }

    protected Board getBoard() {
        return new HexBoard(boardSize, boardSize);
    }
}