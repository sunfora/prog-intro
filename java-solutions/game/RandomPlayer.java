package game;

import java.util.Random;

public class RandomPlayer implements Player {
    private final Random random = new Random();

    @Override
    public Move makeMove(Position position) {
        while (true) {
            final Move move = new Move(
                    getRandomX(position.getField()),
                    getRandomY(position.getField()),
                    position.getTurn()
            );
            if (position.isValid(move)) {
                return move;
            }
        }
    }

    private int getRandomX(Field field) {
        return random.nextInt(field.getRangeX().length()) + field.getMinX();
    }

    private int getRandomY(Field field) {
        return random.nextInt(field.getRangeY().length()) + field.getMinY();
    }
}
