package game;

import java.util.List;
import java.io.PrintStream;

public class TicTacToeTournament extends Circle31Tournament {
    private int m;
    private int n;
    private int k;

    public TicTacToeTournament(int m, int n, int k, List<Player> players, PrintStream out) {
        super(players, out);
        this.n = n;
        this.m = m;
        this.k = k;
    }

    protected Board getBoard() {
        return new TicTacToeBoard(n, m, k);
    }
}