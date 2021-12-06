package game;

import java.io.*;
import java.util.*;

// If PrintStream detects IOException it raises UncheckedIOException
// User should convert it back to IOException as soon as possible
abstract public class Circle31Tournament extends AllPlayAllTournament {

    protected static final String NL = System.lineSeparator();
    protected final Map<Player, Integer> results;
    protected final List<Player> players;
    protected final PrintStream out;

    public Circle31Tournament(List<Player> players, PrintStream out) {
        super(players);
        this.out = out;
        this.players = players;
        results = new HashMap<>();
        for (Player pl : players) {
            results.put(pl, 0);
        }
    }

    protected void regResult(Pair<Player, Player> game, int result) {
        check(game);
        switch (result) {
            case 1:
                results.put(game.first, results.get(game.first) + 3);
                break;
            case 2:
                results.put(game.second, results.get(game.second) + 3);
                break;
            case 0:
                results.put(game.first, results.get(game.first) + 1);
                results.put(game.second, results.get(game.second) + 1);
                break;
            default:
                throw new IllegalArgumentException("Game ended with unsupported result");
        }
    }

    private Pair<Player, Player> check(Pair<Player, Player> game) {
        Objects.requireNonNull(game);
        Objects.requireNonNull(game.first);
        Objects.requireNonNull(game.second);
        return game;
    }

    protected void comment(Pair<Player, Player> game, int results) {
        check(game);
        if (null != out) {
            String msg;
            switch (results) {
                case 1:
                    msg = game.first + " beating " + game.second;
                    break;
                case 2:
                    msg = game.second + " beating " + game.first;
                    break;
                case 0:
                    msg = "draw";
                    break;
                default:
                    msg = "?";
            }
            out.format("Game between %s and %s ended in %s." + NL, game.first, game.second, msg);
        }
    }

    protected void results() {
        if (null != out) {
            out.println("Tournamnet ended!");
            out.println("Here are the results:");
            List<Map.Entry<Player, Integer>> res = new ArrayList<>(results.entrySet());
            res.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));
            for (Map.Entry<Player, Integer> pair : res) {
                out.println(pair.getKey() + " got " + pair.getValue() + " points");
                if (out.checkError()) {
                    throw new UncheckedIOException(
                        new IOException("Print stream caught IOException")
                    );
                }
            }
        }
    }
}