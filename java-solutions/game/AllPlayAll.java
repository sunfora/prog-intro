package game;

import java.util.*;

public class AllPlayAll implements Games {

    private final List<Player> players;
    private final List<IntPair> games;
    private int gamesPlayed = 0;
    private final Random rng = new Random();

    public AllPlayAll(final List<Player> players) {
        this.players = List.copyOf(players);
        games = new ArrayList<>();
        for (int i : new Range(0, players.size() - 1)) {
            for (int j : new Range(i + 1, players.size())) {
                games.add(rng.nextBoolean()? new IntPair(i, j) : new IntPair(j, i));
            }
        }
        Collections.shuffle(games);
    }

    public boolean hasNext() {
        return gamesPlayed != games.size();
    }

    public Pair<Player, Player> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("AllPlayAll ended");
        }
        IntPair pair = games.get(gamesPlayed++);
        return new Pair<>(players.get(pair.x), players.get(pair.y));
    }

    public Iterator<Pair<Player, Player>> iterator() {
        return this;
    }
}