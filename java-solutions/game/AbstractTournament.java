package game;

public abstract class AbstractTournament implements Tournament {

    protected final Games games;

    public AbstractTournament(Games games) {
        this.games = games;
    }

    protected abstract void regResult(Pair<Player, Player> game, int result);

    protected abstract void comment(Pair<Player, Player> game, int result);

    protected abstract void results();

    protected abstract Board getBoard();

    protected int playGame(Pair<Player, Player> game, boolean log) {
        return new TwoPlayerGame(
            getBoard(),
            game.first,
            game.second
        ).play(log);
    }

    public void start(boolean log) {
        for (Pair<Player, Player> game : games) {
            int result = playGame(game, log);
            if (log) {
                comment(game, result);
            }
            regResult(game, result);
        }
        if (log) {
            results();
        }
    }
}