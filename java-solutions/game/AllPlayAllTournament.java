package game;

import java.util.List;

public abstract class AllPlayAllTournament extends AbstractTournament {
    public AllPlayAllTournament(List<Player> players) {
        super(new AllPlayAll(players));
    }
}