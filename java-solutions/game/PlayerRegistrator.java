package game;

import java.util.*;
import java.io.*;

public class PlayerRegistrator implements Closeable {

    private Prettify pretty;
    private boolean askNicks;
    private InputHandler asker;
    private List<Player> players;
    private InputStream in;
    private PrintStream out;

    public PlayerRegistrator(InputStream in, PrintStream out) {
        this.in = in;
        this.out = out;
        asker = new InputHandler(in, out);
        players = new ArrayList<>();
        pretty = new OrtPrettify();
    }

    public PlayerRegistrator setPrettify(Prettify pretty) {
        this.pretty = new PrettyProxy(Objects.requireNonNull(pretty));
        return this;
    }

    public PlayerRegistrator askNicknames(boolean ask) {
        askNicks = ask;
        return this;
    }

    public List<Player> showRegistered() {
        return Collections.unmodifiableList(players);
    }

    public void registerHuman(String message) throws IOException {
        if (askNicks) {
            register(
                new HumanPlayer(
                    in,
                    out,
                    asker.askWithAgreement(asker::readLine, message).second.strip(),
                    pretty
                )
            );
        } else {
            register(new HumanPlayer(in, out, pretty));
        }
    }

    public void register(Player player) {
        players.add(player);
    }

    public void close() {
        asker.close();
    }
}