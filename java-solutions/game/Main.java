package game;

import java.util.Map;
import java.io.UncheckedIOException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        InputHandler asker = new InputHandler(System.in, System.out);
        try {
            ReadingFunction<Integer> atLeastTwo = new ModifyReading<>(
                asker::readInt,
                new Conditional<Integer>(
                    (new Range(2, Integer.MAX_VALUE))::contains
                )
            );
            Pair<Boolean, Integer> result = asker.askWithAgreement(
                atLeastTwo,
                "How many players?",
                "Input must be an integer with value at least 2"
            );
            if (!result.first) {
                System.out.println("Failed to get players cnt");
                return;
            }

            PlayerRegistrator players = new PlayerRegistrator(System.in, System.out)
                .askNicknames(true)
                .setPrettify(
                    new HexPrettify()
                        .showAxis(true)
                        .setWidth(2)
                        .mapByObject(
                            Map.of(
                                Cell.E, "",
                                Cell.B, "",
                                Cell.W, ""
                            )
                        )
                );

            try {
                int boardSize = 11;
                for (int cnt = 0; cnt < result.second; ++cnt) {
 //                   players.register(new RandomPlayer());
                    players.registerHuman(String.format("Enter your nickname, player %d", cnt + 1));
                }
                Tournament tournament = new HexTournament(boardSize, players.showRegistered(), System.out);
                tournament.start(true);
            } catch (UncheckedIOException e) {
                System.out.println(e.getMessage());
                throw e.getCause();
            } finally {
                players.close();
            }
        } catch (IOException e) {
            System.out.println("IOException occured " + e.getMessage());
        } finally {
            asker.close();
        }
    }
}