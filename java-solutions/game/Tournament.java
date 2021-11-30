package game;

import java.util.*;

public class Tournament implements Iterable<Pair<Player, Player>>, Iterator<Pair<Player, Player>> {

    private final List<Player> players;
    private final List<IntPair> games;
    private final Map<Player, Integer> scores;
    private int gamesPlayed = 0;

    public Tournament(List<Player> players) {
        this.players = List.copyOf(players);
        scores = new HashMap<>();
        games = new ArrayList<>();
        for (int i : new Range(0, players.size() - 1)) {
            for (int j : new Range(i + 1, players.size())) {
                games.add(new IntPair(i, j));
            }
        }
        for (Player player: players) {
            scores.put(player, 0);
        }
        Collections.shuffle(games);
    }

    public boolean hasNext() {
        return gamesPlayed != games.size();
    }

    public Pair<Player, Player> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("Tournament ended");
        }
        IntPair pair = games.get(gamesPlayed++);
        return new Pair<>(players.get(pair.x), players.get(pair.y));
    }

    public Iterator<Pair<Player, Player>> iterator() {
        return this;
    }

    public Map<Player, Integer> getScores() {
        return Collections.unmodifiableMap(scores);
    }

    public void regResult(Pair<Player, Player> players, int result) {
        Player f = players.first;
        Player s = players.second;
        switch (result) {
            case 1:
                scores.put(f, scores.get(f) + 3);
                return;
            case 2:
                scores.put(s, scores.get(s) + 3);
                return;
            case 0:
                scores.put(s, scores.get(s) + 1);
                scores.put(s, scores.get(s) + 1);
                return;
            default:
                throw new IllegalArgumentException("Cannot register unknown or invalid game result");
        }
    }
}