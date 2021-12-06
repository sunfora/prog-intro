package game;

import java.util.*;
import java.io.*;
import java.util.function.*;


// HumanPlayer throws Unchecked IOExceptions
// Convert them back to IOException if it is possible
public class HumanPlayer implements Player {

    private static int anon = 0;

    private final PrintStream out;
    private final InputStream in;
    private final InputHandler asker;
    private final String nickname;
    private Position position;
    private Prettify pretty;

    public HumanPlayer(InputStream in, PrintStream out, String nickname, Prettify pretty) {
        this.out = Objects.requireNonNull(out);
        this.in = Objects.requireNonNull(in);
        this.nickname = Objects.requireNonNull(nickname);
        this.pretty = Objects.requireNonNull(pretty);
        asker = new InputHandler(in, out);
    }

    public HumanPlayer(InputStream in, PrintStream out, Prettify pretty) {
        this(in, out, "Unknown_Player_" + ++anon, pretty);
    }

    @Override
    public Move makeMove(Position position) {
        this.position = position;
        try {
            out.println("Current position");
            out.println(pretty.display(position.getField()));
            Result<IntPair> result = asker.askWithAgreement(
                new ModifyReading<>(asker::readIntPair, this::validate),
                String.format("[Player %s] Enter your move : ", nickname),
                "Not valid pair or bad move"
            );
            if (out.checkError()) {
                asker.close();
                throw new UncheckedIOException(new IOException("Print stream got IOException"));
            }
            if (!result.isValid()) {
                throw new NoSuchElementException("Porbably input ended");
            }
            return new Move(result.getValue(), position.getTurn());
        } catch (IOException e) {
            asker.close();
            throw new UncheckedIOException(e);
        }
    }

    public String toString() {
        return nickname;
    }

    private Result<IntPair> validate(Result<IntPair> result) {
        if (result.isValid()) {
            if (!position.isValid(new Move(result.getValue(), position.getTurn()))) {
                result = new Result<>(false, null);
            }
        }
        return result;
    }
}